package com.pdm.segunda_avaliacao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.pdm.segunda_avaliacao.databinding.ActivityMainBinding;
import com.pdm.segunda_avaliacao.model.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    private final int NEW_TASK_REQUEST_CODE = 0;
    private ArrayList<Task> tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ligando (binding) objetos com as Views
        activityMainBinding = activityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
    }

    //Inicializa o Menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.exit){
            FirebaseAuth.getInstance().signOut();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return true;
        }
        return false;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addTask:
                Intent newTaskIntent = new Intent(this, TaskActivity.class);
                startActivityForResult(newTaskIntent, NEW_TASK_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_TASK_REQUEST_CODE && resultCode == RESULT_OK){
            Task contato = (Task) data.getSerializableExtra(Intent.EXTRA_USER);
            if (contato != null){
                tasksList.add(contato);
                // Notificar Adapter
                //Ainda não criado
                //taskAdapter.notifyDataSetChanged();
                //Lógica para adição ao banco?
            }
        }
    }
}