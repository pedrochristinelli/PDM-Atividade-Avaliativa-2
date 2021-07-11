package com.pdm.segunda_avaliacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pdm.segunda_avaliacao.databinding.ActivityRegisterBinding;
import com.pdm.segunda_avaliacao.model.LocalUser;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding activityRegisterBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ligando (binding) objetos com as Views
        activityRegisterBinding = activityRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterBinding.getRoot());
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonRegister:
                String password = activityRegisterBinding.editTextTextPassword.getText().toString();
                String confirmPassword = activityRegisterBinding.editTextTextConfirmPassword.getText().toString();
                String email = activityRegisterBinding.editTextTextEmailAddress.getText().toString();
                String username = activityRegisterBinding.editTextTextUsername.getText().toString();

                if(password.equals(confirmPassword)){
                    if(password.length() <= 6){
                        LocalUser localUser = new LocalUser();
                        localUser.setEmail(email);
                        localUser.setUsername(username);

                        //Cria o usuário no Firestore
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = auth.getCurrentUser();
                                            assert user != null;
                                            //Salva o ID
                                            localUser.setId(user.getUid());

                                            db.collection("user")
                                                    .document(localUser.getId())
                                                    .set(localUser.toMap());

                                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();

                                        } else {
                                            System.out.println(task.getException().getMessage());
                                            activityRegisterBinding.warningMessage.setVisibility(View.VISIBLE);
                                            activityRegisterBinding.warningMessage.setText("Não foi possível criar o usuário");
                                        }
                                    }
                                });
                    }   else{
                        activityRegisterBinding.warningMessage.setVisibility(View.VISIBLE);
                        activityRegisterBinding.warningMessage.setText("A senha deve conter no minímo 6 caracteres");
                    }
                }   else{
                    activityRegisterBinding.warningMessage.setVisibility(View.VISIBLE);
                    activityRegisterBinding.warningMessage.setText("As senhas não conferem");
                }
                break;
            case R.id.buttonEnter:
                //Fecha a intent
                finish();
                break;
        }
    }
}