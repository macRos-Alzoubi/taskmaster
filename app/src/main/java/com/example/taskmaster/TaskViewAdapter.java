package com.example.taskmaster;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.TaskViewHolder> {
    private List<TaskModel> taskList;

    public TaskViewAdapter(List<TaskModel> taskList) {
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
        holder.taskModel = taskList.get(position);
        TextView textView = holder.view.findViewById(R.id.text_task_title);
        textView.setText(holder.taskModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TaskModel taskModel;

        public View view;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), TaskDetail.class);
            intent.putExtra("title", taskModel.getTitle());
            intent.putExtra("body", taskModel.getBody());
            intent.putExtra("status", taskModel.getStatus());
            view.getContext().startActivity(intent);
        }
    }
}
