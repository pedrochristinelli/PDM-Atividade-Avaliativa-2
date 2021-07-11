package com.pdm.segunda_avaliacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pdm.segunda_avaliacao.databinding.ActivityTaskBinding;
import com.pdm.segunda_avaliacao.model.Task;
import com.pdm.segunda_avaliacao.utils.UserFirebaseRequest;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {
    ActivityTaskBinding activityTaskBinding;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTaskBinding = activityTaskBinding.inflate(getLayoutInflater());
        setContentView(activityTaskBinding.getRoot());
    }

    public void onClick(View view) throws ParseException {
        switch (view.getId()){
            case R.id.buttonActionTask:
                Date date = new Date();
                Task task = new Task();
                task.setTitle(activityTaskBinding.editTextTextTitle.getText().toString());
                task.setCreatedBy(auth.getUid());
                task.setDescription(activityTaskBinding.editTextTextDescription.getText().toString());

                //Data prevista
                Date dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse(activityTaskBinding.editTextEndingDate.getText().toString());

                task.setEndingForecastTime(dateEnd.getTime());
                task.setCreatedAt(date.getTime());
                task.setStatus(1);

                if(!task.getTitle().equals("") && !task.getDescription().equals("") && !activityTaskBinding.editTextEndingDate.getText().toString().equals("")){
                    //Salva o título como ID, dessa forma, nunca haverá duplicação
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("tasks")
                            .document(task.getTitle())
                            .set(task.toMap());

                    Intent retornoIntent =  new Intent();
                    retornoIntent.putExtra(Intent.EXTRA_USER, task);

                    //retornoIntent.putExtra(Intent.EXTRA_INDEX, posicao);
                    setResult(RESULT_OK, retornoIntent);
                    finish();
                }   else{
                    activityTaskBinding.warningMessage.setVisibility(View.VISIBLE);
                    activityTaskBinding.warningMessage.setText("Preencha todos os campos!");
                }
                break;
        }
    }
}