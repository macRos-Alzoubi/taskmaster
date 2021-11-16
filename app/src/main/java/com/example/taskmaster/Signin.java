package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

public class Signin extends AppCompatActivity {

    private static final String TAG = "SigninActivity";
    private static final String ERROR_MSG = "Email and/or password incorrect!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        amplifyConfig();

        EditText password = findViewById(R.id.signin_password);
        EditText email = findViewById(R.id.signin_email);
        TextView errorLabel = findViewById(R.id.signin_error_label);

        findViewById(R.id.signin_btn).setOnClickListener(view -> Amplify.Auth.signIn(
                email.getText().toString(),
                password.getText().toString(),
                result -> startActivity(new Intent(getApplicationContext(), MainActivity.class)),
                error -> errorLabel.setText(ERROR_MSG)
        ));

        findViewById(R.id.signin_signup_btn).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Signup.class)));
    }

    private void amplifyConfig() {
        try {
            Amplify.addPlugin(new AWSDataStorePlugin()); // stores records locally
            Amplify.addPlugin(new AWSApiPlugin()); // stores things in DynamoDB and allows us to perform GraphQL queries
            Amplify.addPlugin(new AWSCognitoAuthPlugin()); // Add this line, to include the Auth plugin.
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }
    }
}