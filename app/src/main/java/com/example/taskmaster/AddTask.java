package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {
    private int tasksNumber = 0;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button button = findViewById(R.id.button_add_task);

        button.setOnClickListener(View -> {
            TextView textView = findViewById(R.id.text_total_task);
            tasksNumber++;
            textView.setText("Total Tasks: " + tasksNumber);
            Toast toast = Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG);
            toast.show();
        });
    }
}