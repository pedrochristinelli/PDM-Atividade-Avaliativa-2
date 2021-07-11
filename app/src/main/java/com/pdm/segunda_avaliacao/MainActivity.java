package com.pdm.segunda_avaliacao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pdm.segunda_avaliacao.databinding.ActivityMainBinding;
import com.pdm.segunda_avaliacao.model.Task;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    private final int NEW_TASK_REQUEST_CODE = 0;
    private ArrayList<Task> tasksList = new ArrayList<>();
    private TaskAdapter taskAdapter;

    public void populateArrayList(){
        for (int i = 0; i < 10; i++) {
            Task task = new Task();

            task.setCreatedAt(new Date().getTime());
            task.setTitle("Titulo #"+i);
            task.setDescription("Descrizao do titulo #"+1);
            task.setStatus(1);

            if (i==3 || i==6) task.setStatus(3);
            if (i==2 || i==8 || i ==9) task.setStatus(2);

            if (task.getStatus() == 3) task.setEndedBy("Usuario#"+i);

            tasksList.add(task);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ligando (binding) objetos com as Views
        populateArrayList();
        activityMainBinding = activityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        taskAdapter = new TaskAdapter(this, android.R.layout.simple_list_item_1, tasksList);

        activityMainBinding.tasksLv.setAdapter(taskAdapter);

        registerForContextMenu(activityMainBinding.tasksLv);

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_task, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo());
        Task task = tasksList.get(menuInfo.position);

        switch (item.getItemId()){
            case R.id.completeTask:
                Toast.makeText(this, "completeTask", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.editTask:
                Toast.makeText(this, "editTask", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.deleteTask:
                Toast.makeText(this, "deleteTask", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
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
                taskAdapter.notifyDataSetChanged();
                //Lógica para adição ao banco?
            }
        }
    }
}