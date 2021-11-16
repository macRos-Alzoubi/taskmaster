package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.core.Amplify;

public class ConfirmAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_account);

        EditText confirmCode = findViewById(R.id.confirm_code);
        Intent intent = getIntent();

        findViewById(R.id.confirm_btn).setOnClickListener(view -> Amplify.Auth.confirmSignUp(
                intent.getStringExtra("email"),
                confirmCode.getText().toString(),
                result -> userSignin(intent.getStringExtra("email"), intent.getStringExtra("password")),
                error -> Log.e("AuthQuickstart", error.toString())
        ));

        findViewById(R.id.confirm_resend_code_btn).setOnClickListener(view -> resendVeriCode());
    }

    private void resendVeriCode() {
        Amplify.Auth.resendUserAttributeConfirmationCode(AuthUserAttributeKey.email(),
                result -> Log.i("AuthDemo", "Code was sent again: " + result.toString()),
                error -> Log.e("AuthDemo", "Failed to resend code.", error)
        );
    }

    private void userSignin(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                result -> startActivity(new Intent(getApplicationContext(), MainActivity.class)),
                error -> startActivity(new Intent(getApplicationContext(), Signin.class)));
    }
}