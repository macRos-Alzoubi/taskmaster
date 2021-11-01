package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button_add_task);

        button.setOnClickListener(View -> {
            Intent intent = new Intent(MainActivity.this, AddTask.class);
            startActivity(intent);
        });

        Button allTasksButton = findViewById(R.id.button_all_tasks);

        allTasksButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(intent);
        });

        findViewById(R.id.button_settings).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsPage.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString("username","username");

        TextView usernameText = findViewById(R.id.main_text_username);
        System.out.println(usernameText);
        usernameText.setText(username);
    }

    public void goToTaskDetail(View view){
        int id = view.getId();
        TextView textView = findViewById(id);
        Intent intent = new Intent(MainActivity.this, TaskDetail.class);
        intent.putExtra("taskTitle", textView.getText());
        startActivity(intent);
    }
}