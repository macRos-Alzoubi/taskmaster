package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class Signup extends AppCompatActivity {
    private static final String ERROR_MSG = "Sign up failed";
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText password = findViewById(R.id.signup_password);
        EditText email = findViewById(R.id.signup_email);
        EditText username = findViewById(R.id.signup_username);
        TextView errorLabel = findViewById(R.id.signup_error_label);

        findViewById(R.id.signup_btn).setOnClickListener(view -> {

            Log.i(TAG, "email: " + email.getText().toString());
            Log.i(TAG, "password: " + password.getText().toString());
            Log.i(TAG, "username: " + username.getText().toString());
            AuthSignUpOptions options = AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.name(), username.getText().toString())
                    .build();
            Amplify.Auth.signUp(email.getText().toString(), password.getText().toString(), options,
                    result -> {
                        Intent intent = new Intent(getApplicationContext(), ConfirmAccount.class);
                        intent.putExtra("email", email.getText().toString());
                        intent.putExtra("password", password.getText().toString());
                        startActivity(intent);
                    },
                    error -> errorLabel.setText(ERROR_MSG)
            );
        });

        findViewById(R.id.signup_signin_btn).setOnClickListener(
                view -> startActivity(new Intent(getApplicationContext(), Signin.class))
        );
    }
}