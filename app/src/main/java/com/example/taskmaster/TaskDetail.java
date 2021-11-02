package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();

        String taskTitle = intent.getStringExtra("title");
        TextView titleTextView = findViewById(R.id.text_detail_task_title);
        titleTextView.setText(taskTitle);

        String taskBody = intent.getStringExtra("body");
        TextView bodyTextView = findViewById(R.id.text_detail_task_description);
        bodyTextView.setText(taskBody);

        String taskStatus = intent.getStringExtra("status");
        TextView StatusTextView = findViewById(R.id.text_detail_task_status);
        StatusTextView.setText(taskStatus);

    }
}