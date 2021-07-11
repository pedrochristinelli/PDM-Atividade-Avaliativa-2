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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pdm.segunda_avaliacao.databinding.ActivityMainBinding;
import com.pdm.segunda_avaliacao.model.Task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final int EDIT_TASK_REQUEST_CODE = 1;
    ActivityMainBinding activityMainBinding;
    public final int NEW_TASK_REQUEST_CODE = 0;
    private ArrayList<Task> tasksList = new ArrayList<>();
    private TaskAdapter taskAdapter;
    private FirebaseAuth auth;

    public void populateArrayList(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Task task1 = new Task();
                                task1.setCreatedAt((Long) document.getData().get("createdAt"));
                                task1.setCreatedBy((String) document.getData().get("createdBy"));
                                task1.setEndedAt((long) document.getData().get("endedAt"));
                                task1.setDescription((String) document.getData().get("description"));
                                task1.setEndedBy((String) document.getData().get("endedBy"));
                                task1.setTitle((String) document.getData().get("title"));
                                task1.setEndingForecastTime((Long) document.getData().get("endingForecastTime"));
                                task1.setStatus(((Long) document.getData().get("status")).intValue());
                                task1.setCreatedByUsername((String) document.getData().get("createdByUsername"));
                                task1.setEndedByUsername((String) document.getData().get("endedByUsername"));
                                tasksList.add(task1);
                            }

                            taskAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ligando (binding) objetos com as Views
        activityMainBinding = activityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        //Popula a lista
        populateArrayList();

        //Inicializa Auth
        auth = FirebaseAuth.getInstance();

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        switch (item.getItemId()){
            case R.id.completeTask:
                Date date = new Date();
                task.setStatus(3);
                task.setEndedAt(date.getTime());
                task.setEndedBy(auth.getUid());

                db.collection("user").document(task.getEndedBy()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<DocumentSnapshot> taskGoogle) {
                        DocumentSnapshot documentSnapshot = taskGoogle.getResult();
                        if (documentSnapshot.exists()){
                            task.setEndedByUsername((String) documentSnapshot.getData().get("username").toString());
                        }
                    }
                });

                db.collection("tasks").document(task.getTitle()).update(task.toMap());
                tasksList.clear();
                populateArrayList();
                return true;
            case R.id.editTask:
                Intent editTaskIntent = new Intent(this, TaskActivity.class);
                editTaskIntent.putExtra(Intent.EXTRA_USER, task);
                editTaskIntent.putExtra(Intent.EXTRA_INDEX, menuInfo.position);
                startActivityForResult(editTaskIntent, EDIT_TASK_REQUEST_CODE);
                return true;
            case R.id.deleteTask:
                if(task.getStatus() != 3){
                    db.collection("tasks").document(task.getTitle()).delete();
                    tasksList.remove(task);
                    taskAdapter.notifyDataSetChanged();
                }   else{
                    activityMainBinding.warningMessage.setText("Não é possível deletar uma tarefa finalizada");
                }
                return true;
            case R.id.assumeTask:
                task.setStatus(2);
                db.collection("tasks").document(task.getTitle()).update(task.toMap());
                tasksList.clear();
                populateArrayList();
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
                taskAdapter.notifyDataSetChanged();
                //Lógica para adição ao banco?
            }
        }
    }
}