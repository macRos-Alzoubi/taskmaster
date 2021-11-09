package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

public class AddTask extends AppCompatActivity {
    private static final String TAG = "AddTask";
    Long taskCount;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Spinner teamSpinner = findViewById(R.id.team_spinner);
        Spinner statusSpinner = findViewById(R.id.task_status_dropdown);

        ArrayAdapter<CharSequence> statusListAdapter = ArrayAdapter.createFromResource(this, R.array.task_status, android.R.layout.simple_spinner_item);
        statusListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusListAdapter);

        ArrayAdapter<CharSequence> teamArrayAdapter = ArrayAdapter.createFromResource(this, R.array.teams, android.R.layout.simple_spinner_item);
        teamArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(teamArrayAdapter);



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
            String taskStatus = statusSpinner.getSelectedItem().toString();
            String tasksTeam =  teamSpinner.getSelectedItem().toString();

//            saveToDataStore(taskTitle, taskDescription, taskStatus);
            saveToApi(taskTitle, taskDescription, taskStatus);

            taskCount = sharedPreferences.getLong("taskCount", 0);
            editor.putLong("taskCount", taskCount + 1);
            editor.putString("team", tasksTeam);
            editor.apply();

            Toast toast = Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG);
            toast.show();

            TextView textView = findViewById(R.id.text_total_task);
            textView.setText("Total Tasks: " + (taskCount + 1));
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

    private void saveToDataStore(String taskTitle, String taskDescription, String taskStatus) {

        Task task = Task.builder().title(taskTitle).description(taskDescription).status(taskStatus).build();
        Amplify.DataStore.save(task,
                success -> Log.i(TAG, "Saved item: " + success.item().getTitle()),
                error -> Log.i(TAG, "Saved item: " + error.getMessage()));
    }

    private void saveToApi(String taskTitle, String taskDescription, String taskStatus) {
        Task task = Task.builder().title(taskTitle).description(taskDescription).status(taskStatus).build();

        System.out.println(task.toString());

        Amplify.API.mutate(ModelMutation.create(task),
                success -> Log.i(TAG, "Saved item: " + success.getData().getTitle()),
                error -> Log.i(TAG, "Saved item: " + error.getMessage()));
    }
}
