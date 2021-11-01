package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        findViewById(R.id.button_save).setOnClickListener(view -> {
            TextView usernameField = findViewById(R.id.text_username);
            if(!usernameField.getText().toString().equals("")){
                editor.putString("username", usernameField.getText().toString());
                editor.apply();
                usernameField.setText("");
            }
        });
    }
}