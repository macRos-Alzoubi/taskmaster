package com.example.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    List<Task> taskList = new ArrayList<>();
    RecyclerView recyclerView;
    Handler handler;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amplifyConfig();
//        seedTeams();

        recyclerView = findViewById(R.id.task_recyleView);

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

        getDataFromDynamoDBApi();

        handler = new Handler(Looper.getMainLooper(), message -> {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new TaskViewAdapter(taskList));
            recyclerView.getAdapter().notifyDataSetChanged();
            return false;
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {

        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString("username", "username");

        TextView usernameText = findViewById(R.id.main_text_username);
        usernameText.setText(username);
        getDataFromDynamoDBApi();
    }

    private void seedTeams() {
        String[] teams = getResources().getStringArray(R.array.teams);
        Team teamObj;

        for (String team : teams) {
            teamObj = Team.builder().name(team).build();
            Amplify.API.mutate(ModelMutation.create(teamObj),
                    res -> Log.i("MainActivity", String.format("Team %s has been successfully saved!", team)),
                    error -> Log.i("MainActivity", error.getMessage()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDataFromDynamoDBApi() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPreferences.getString("team", null);

        if (name != null) {
            final Team[] team = new Team[1];
            System.out.println("team name: " + name);
            Amplify.API.query(ModelQuery.list(Team.class, Team.NAME.contains(name)),

                    res -> {
                        res.getData().forEach(team1 -> team[0] = team1);
                        if (team[0] != null) {
                            Amplify.API.query(ModelQuery.list(Task.class, Task.TEAM_ID.eq(team[0].getId())),// edit team_id and add team obj or team name and query by it
                                    success -> {
                                        taskList = new ArrayList<>();
                                        success.getData().forEach(task -> taskList.add(task));
                                        System.out.println("Task List:" + taskList);
                                        handler.sendEmptyMessage(1);
                                    },
                                    error -> Log.e(TAG, "Could not initialize Amplify", error));
                        }
                    }
                    , error -> Log.i("MainActivity", error.getMessage()));
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a team from settings page!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void amplifyConfig() {
        try {
            Amplify.addPlugin(new AWSDataStorePlugin()); // stores records locally
            Amplify.addPlugin(new AWSApiPlugin()); // stores things in DynamoDB and allows us to perform GraphQL queries
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }
    }
}
