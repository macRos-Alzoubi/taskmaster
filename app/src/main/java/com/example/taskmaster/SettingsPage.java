package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Spinner teamSpinner = findViewById(R.id.settings_teams_spinner);

        ArrayAdapter<CharSequence> teamArrayAdapter = ArrayAdapter.createFromResource(this, R.array.teams, android.R.layout.simple_spinner_item);
        teamArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(teamArrayAdapter);

        findViewById(R.id.button_save).setOnClickListener(view -> {
            TextView usernameField = findViewById(R.id.text_username);
            if(!usernameField.getText().toString().equals("")){
                editor.putString("username", usernameField.getText().toString());
                editor.putString("team", teamSpinner.getSelectedItem().toString());

                editor.apply();
                usernameField.setText("");
            }
        });
    }
}