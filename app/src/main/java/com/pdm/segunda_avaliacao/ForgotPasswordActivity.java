package com.pdm.segunda_avaliacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pdm.segunda_avaliacao.databinding.ActivityForgotPasswordBinding;

import java.util.Timer;
import java.util.TimerTask;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding activityForgotPasswordBinding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        // Ligando (binding) objetos com as Views
        activityForgotPasswordBinding = activityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(activityForgotPasswordBinding.getRoot());
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonForgotPassword:
                String email = activityForgotPasswordBinding.editTextTextEmailAddress.getText().toString();
                activityForgotPasswordBinding.warningMessage.setVisibility(View.VISIBLE);
                if(!email.equals("")){
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        activityForgotPasswordBinding.warningMessage.setTextColor(0xff673AB7);
                                        activityForgotPasswordBinding.warningMessage.setText("Email enviado!");

                                        int delay = 5000;
                                        int interval = 1000;
                                        Timer timer = new Timer();
                                        timer.scheduleAtFixedRate(new TimerTask() {
                                            public void run() {
                                                timer.cancel();
                                                finish();
                                            }
                                        }, delay, interval);

                                    }   else{
                                        activityForgotPasswordBinding.warningMessage.setTextColor(Color.RED);
                                        activityForgotPasswordBinding.warningMessage.setText("Não foi possível enviar o email!");
                                    }
                                }
                            });
                }   else{
                    activityForgotPasswordBinding.warningMessage.setTextColor(Color.RED);
                    activityForgotPasswordBinding.warningMessage.setText("Preencha o email");
                }
                break;
        }
    }
}