package com.upec.androidtemplate20192020;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mSignupButton, mLoginButton;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        if (sessionManager.isLogged()){
            Intent intent2 = new Intent(this, MainUserActivity.class);
            startActivity(intent2);
            finish();
        }

        mSignupButton = (Button) findViewById(R.id.activity_main_signup_button);
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupActivity = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(signupActivity);
            }
        });
        mLoginButton = (Button) findViewById(R.id.activity_main_login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginActivity);
            }
        });
    }
}