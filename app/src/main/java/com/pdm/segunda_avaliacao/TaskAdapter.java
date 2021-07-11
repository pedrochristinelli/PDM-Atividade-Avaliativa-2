package com.pdm.segunda_avaliacao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pdm.segunda_avaliacao.databinding.ViewTaskBinding;
import com.pdm.segunda_avaliacao.model.Task;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {
    TaskViewHolder taskViewHolder;

    public TaskAdapter(Context context, int layout, ArrayList<Task> tasks){
        super(context, layout, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewTaskBinding viewTaskBinding;

        if (convertView ==null){
            viewTaskBinding = ViewTaskBinding.inflate(LayoutInflater.from(getContext()));

            convertView = viewTaskBinding.getRoot();

            taskViewHolder = new TaskViewHolder();
            taskViewHolder.taskTitleTextView = convertView.findViewById(R.id.taskTitleTextView);
            taskViewHolder.taskDescriptionTextView = convertView.findViewById(R.id.taskDescriptionTextView);
            taskViewHolder.taskForecastDateTextView = convertView.findViewById(R.id.taskForecastDateTextView);
            taskViewHolder.taskStatusTextView = convertView.findViewById(R.id.taskStatusTextView);
            taskViewHolder.taskFinalizedUserTextView = convertView.findViewById(R.id.taskFinalizedUserTextView);

            convertView.setTag(taskViewHolder);
        }
        taskViewHolder = (TaskViewHolder)convertView.getTag();

        Task task = getItem(position);
        taskViewHolder.taskTitleTextView.setText(task.getTitle());
        taskViewHolder.taskDescriptionTextView.setText(task.getDescription());
        taskViewHolder.taskForecastDateTextView.setText(task.getEndingForecastTimeString());
        taskViewHolder.taskStatusTextView.setText(task.getStatusString());

        if (task.getStatus() == 3){
            convertView.setBackgroundColor(0xffbaffc9);
            taskViewHolder.taskFinalizedUserTextView.setText(task.getEndedByUsername());
            convertView.findViewById(R.id.taskFinalizedUserTextView).setVisibility(View.VISIBLE);
        } else if (task.getStatus() == 2){
            convertView.setBackgroundColor(0xffffffba);
            taskViewHolder.taskFinalizedUserTextView.setText("");
            convertView.findViewById(R.id.taskFinalizedUserTextView).setVisibility(View.GONE);
        } else {
            convertView.setBackgroundColor(0xffffdfba);
            taskViewHolder.taskFinalizedUserTextView.setText("");
            convertView.findViewById(R.id.taskFinalizedUserTextView).setVisibility(View.GONE);
        }
        return convertView;
    }

    private class TaskViewHolder {
        public TextView taskTitleTextView;
        public TextView taskDescriptionTextView;
        public TextView taskForecastDateTextView;
        public TextView taskStatusTextView;
        public TextView taskFinalizedUserTextView;
    }
}
