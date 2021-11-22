package com.example.taskmaster;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;

import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.TaskViewHolder> {
    private final List<Task> taskList;

    public TaskViewAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_list, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = taskList.get(position);
        TextView textView = holder.view.findViewById(R.id.text_task_title);
        textView.setText(holder.task.getTitle());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Task task;

        public View view;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), TaskDetail.class);
            intent.putExtra("title", task.getTitle());
            intent.putExtra("body", task.getDescription());
            intent.putExtra("status", task.getStatus());
            intent.putExtra("imageUrl", task.getImgUrl());
            intent.putExtra("lat", task.getLat());
            intent.putExtra("lon", task.getLon());
            view.getContext().startActivity(intent);
        }
    }
}
