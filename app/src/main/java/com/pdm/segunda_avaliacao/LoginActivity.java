package com.pdm.segunda_avaliacao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pdm.segunda_avaliacao.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 12312312;
    private ActivityLoginBinding activityLoginBinding;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BD
        auth = FirebaseAuth.getInstance();

        //Google Login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Ligando (binding) objetos com as Views
        activityLoginBinding = activityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonBegginRegister:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.loginGoogle:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.buttonLogin:
                String email = activityLoginBinding.editTextTextEmailAddress.getText().toString();;
                String password = activityLoginBinding.editTextTextPassword.getText().toString();

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                } else {
                                    activityLoginBinding.warningMessage.setText("Email ou senha não conferem");
                                }
                            }
                        });
                break;
            case R.id.buttonForgotPassword:
                Intent forgotPasswordIntent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                activityLoginBinding.warningMessage.setText("Erro na autenticação");
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            activityLoginBinding.warningMessage.setText("Erro na autenticação");
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Verifica se ele já está logado via google
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Verifica se ele já está logado via email e senha
        FirebaseUser currentUser = auth.getCurrentUser();

        //Redireciona
        if(currentUser != null || account != null){
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}
