package com.example.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static PinpointManager pinpointManager;
    List<Task> taskList = new ArrayList<>();
    RecyclerView recyclerView;
    Handler tasksHandler, usernameHandler;

    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i("INIT", userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        final String token = task.getResult();
                        Log.d(TAG, "Registering push notifications token: " + token);
                        pinpointManager.getNotificationClient().registerDeviceToken(token);
                    });
        }
        return pinpointManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendAnalytics(this.toString(), Signin.class.toString());

//        seedTeams();

        recyclerView = findViewById(R.id.task_recyleView);
        Button button = findViewById(R.id.button_add_task);
        Button allTasksButton = findViewById(R.id.button_all_tasks);
        TextView username = findViewById(R.id.main_text_username);

        getUserName();
        getDataFromDynamoDBApi();
        final PinpointManager pinpointManager = getPinpointManager(getApplicationContext());
        pinpointManager.getSessionClient().startSession();

        button.setOnClickListener(View -> {
            Intent intent = new Intent(MainActivity.this, AddTask.class);
            startActivity(intent);
        });

        allTasksButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(intent);
        });

        findViewById(R.id.button_settings).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsPage.class);
            startActivity(intent);
        });

        findViewById(R.id.main_activity_logout_btn).setOnClickListener(view -> logout());

        usernameHandler = new Handler(Looper.getMainLooper(), message -> {
            username.setText(message.getData().getString("username"));
            return false;
        });

        tasksHandler = new Handler(Looper.getMainLooper(), message -> {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new TaskViewAdapter(taskList));
            recyclerView.getAdapter().notifyDataSetChanged();
            return false;
        });

    }

    public static void sendAnalytics(String current, String prev) {
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("Activity Changed")
                .addProperty("Current Activity", current)
                .addProperty("Prev", prev)
                .build();

        Amplify.Analytics.recordEvent(event);
    }

    private void logout() {
        Amplify.Auth.signOut(
                () -> startActivity(new Intent(getApplicationContext(), Signin.class)),
                error -> Log.e("AuthQuickstart", error.toString())
        );
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

    public void getUserName() {
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", attributes.get(2).getValue());
                    msg.setData(bundle);
                    usernameHandler.sendMessage(msg);
                },
                error -> Log.e("AuthDemo", "Failed to fetch user attributes.", error)
        );
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
                                        tasksHandler.sendEmptyMessage(1);
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
}
