package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {
    Long taskCount;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Spinner spinner = findViewById(R.id.task_status_dropdown);

        ArrayAdapter<CharSequence> statusListAdapter = ArrayAdapter.createFromResource(this, R.array.task_status, android.R.layout.simple_spinner_item);
        statusListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(statusListAdapter);

        Button button = findViewById(R.id.button_add_task);

        //
        //save button onClickListener handler
        button.setOnClickListener(View -> {
            TextView task_title = findViewById(R.id.text_task_title_2);
            TextView task_description = findViewById(R.id.text_task_description);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

            String taskTitle = task_title.getText().toString();
            String taskDescription = task_description.getText().toString();
            String taskStatus = spinner.getSelectedItem().toString();

            TaskModel taskModel = new TaskModel(taskTitle, taskDescription, taskStatus);

            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            taskCount = db.taskDao().insertTask(taskModel);

            editor.putLong("taskCount", taskCount);
            editor.apply();
            Toast toast = Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG);
            toast.show();

            TextView textView = findViewById(R.id.text_total_task);
            textView.setText("Total Tasks: " + taskCount);
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        taskCount = sharedPreferences.getLong("taskCount", 0);
        System.out.println(taskCount);
        TextView textView = findViewById(R.id.text_total_task);
        textView.setText("Total Tasks: " + taskCount);
    }
}