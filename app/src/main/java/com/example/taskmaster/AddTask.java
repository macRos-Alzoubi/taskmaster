package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import br.com.onimur.handlepathoz.HandlePathOz;
import br.com.onimur.handlepathoz.HandlePathOzListener;
import br.com.onimur.handlepathoz.model.PathOz;


import static android.content.Intent.ACTION_PICK;
import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI;

import org.apache.commons.io.FilenameUtils;

import java.io.File;


public class AddTask extends AppCompatActivity implements HandlePathOzListener.SingleUri {
    private static final String TAG = "AddTask";
    private static final int REQUEST_PERMISSION = 123;
    private static final int REQUEST_OPEN_GALLERY = 1111;
    private HandlePathOz handlePathOz;
    private Handler taskFileHandler;
    private String taskFileUrl = "";
//    ActivityResultLauncher<Intent> fileLoader;


    Long taskCount;

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        initHandlePathOz();
        findViewById(R.id.addTask_fileUpload_btn).setOnClickListener(view -> {
            openFile();
        });


//        fileLoader = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        // There are no request codes
//
//                        Intent data = result.getData();
//                    }
//                });

        taskFileHandler = new Handler(Looper.getMainLooper(), message -> {
            taskFileUrl = message.getData().getString("taskFileUrl");
            return false;
        });

        Button button = findViewById(R.id.button_add_task);
        //
        //save button onClickListener handler
        button.setOnClickListener(View -> {

            TextView task_title = findViewById(R.id.addTask_task_name);
            TextView task_description = findViewById(R.id.addTask_task_description);

            if (task_title.getText().length() > 0 && task_description.getText().length() > 0) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

                String taskTitle = task_title.getText().toString();
                String taskDescription = task_description.getText().toString();
                String taskStatus = statusSpinner.getSelectedItem().toString();
                String tasksTeam = teamSpinner.getSelectedItem().toString();

//            saveToDataStore(taskTitle, taskDescription, taskStatus);
                saveToApi(tasksTeam, taskTitle, taskDescription, taskStatus, taskFileUrl);

                taskCount = sharedPreferences.getLong("taskCount", 0);
                editor.putLong("taskCount", taskCount + 1);
                editor.apply();

                Toast toast = Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG);
                toast.show();

                TextView textView = findViewById(R.id.text_total_task);
                textView.setText("Total Tasks: " + (taskCount + 1));

                task_title.setText("");
                task_description.setText("");
            }
        });
    }

    private void openFile() {
        if (checkSelfPermission()) {
            Intent intent;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                intent = new Intent(ACTION_PICK, EXTERNAL_CONTENT_URI);
            } else {
                intent = new Intent(ACTION_PICK, INTERNAL_CONTENT_URI);
            }

            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra("return-data", true);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(intent, REQUEST_OPEN_GALLERY);
//            fileLoader.launch(intent);
        }
    }

    private boolean checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            return false;
        }
        return true;
    }

    private void initHandlePathOz() {
        handlePathOz = new HandlePathOz(this, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        taskCount = sharedPreferences.getLong("taskCount", 0);
        TextView textView = findViewById(R.id.text_total_task);
        textView.setText("Total Tasks: " + taskCount);
    }

    private void saveToDataStore(String name, String taskTitle, String taskDescription, String taskStatus) {

        final Team[] team = new Team[1];
        Amplify.API.query(ModelQuery.get(Team.class, name),
                res ->
                        team[0] = Team.builder().name(name).id(res.getData().getId()).build()
                , error -> Log.i("MainActivity", error.getMessage()));

        Task task = Task.builder().title(taskTitle).teamId(team[0].getId()).description(taskDescription).status(taskStatus).build();

        Amplify.DataStore.save(task,
                success -> Log.i(TAG, "Saved item: " + success.item().getTitle()),
                error -> Log.i(TAG, "Saved item: " + error.getMessage()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveToApi(String name, String taskTitle, String taskDescription, String taskStatus, String taskFileUrl) {

        final Team[] team = new Team[1];
        Amplify.API.query(ModelQuery.list(Team.class, Team.NAME.contains(name)),
                res -> {
                    res.getData().forEach(team1 -> team[0] = team1);

                    Task task = Task.builder()
                            .title(taskTitle)
                            .teamId(team[0].getId())
                            .imgUrl(taskFileUrl)
                            .description(taskDescription)
                            .status(taskStatus).build();

                    Amplify.API.mutate(ModelMutation.create(task),
                            success -> Log.i(TAG, "Saved item: "),
                            error -> Log.i(TAG, "Saved item: " + error.getMessage()));
                }
                , error -> Log.i("MainActivity", error.getMessage()));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                //set Uri to handle
                handlePathOz.getRealPath(uri);
            }
        }
    }

    @Override
    public void onRequestHandlePathOz(@NonNull PathOz pathOz, @Nullable Throwable throwable) {
        if (throwable != null) {
            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
        String filePath = FilenameUtils.getName(pathOz.getPath());
        File file = new File(pathOz.getPath());
        Amplify.Storage.uploadFile(
                filePath,
                file,
                result -> {
                    Amplify.Storage.getUrl(
                            result.getKey(),
                            resultUrl -> {
                                Bundle bundle = new Bundle();
                                bundle.putString("taskFileUrl", resultUrl.getUrl().toString());
                                Message message = new Message();
                                message.setData(bundle);
                                taskFileHandler.sendMessage(message);
                            },
                            error -> Log.e("MyAmplifyApp", "URL generation failure", error)
                    );
                },
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFile();
            } else {
                Log.i(TAG, "Error : Permission Field");
            }
        }
    }
}
