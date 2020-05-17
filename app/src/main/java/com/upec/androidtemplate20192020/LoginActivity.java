package com.upec.androidtemplate20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputLayout;
import com.upec.androidtemplate20192020.dbRequest.dbRequest;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mUsernameTil, mPasswordTil;
    private Button mLoginButton;
    private dbRequest request;
    private RequestQueue queue;
    private ProgressBar mPBar;
    private Handler handler;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        if (intent.hasExtra("SIGNUP")){
            Toast.makeText(this, intent.getStringExtra("SIGNUP"), Toast.LENGTH_SHORT).show();
        }
        if (intent.hasExtra("UPDATESUCCESS")){
            Toast.makeText(this, intent.getStringExtra("UPDATESUCCESS"), Toast.LENGTH_SHORT).show();
        }

        mUsernameTil = (TextInputLayout) findViewById(R.id.activity_login_username_til);
        mPasswordTil = (TextInputLayout) findViewById(R.id.activity_login_password_til);
        mLoginButton = (Button) findViewById(R.id.activity_login_login_button);
        mPBar = (ProgressBar) findViewById(R.id.activity_login_pb_loader);

        handler = new Handler();
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new dbRequest(this, queue);
        sessionManager = new SessionManager(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = mUsernameTil.getEditText().getText().toString().trim();
                final String password = mPasswordTil.getEditText().getText().toString().trim();
                mPBar.setVisibility(View.VISIBLE);
                if (username.length()>0 && password.length()>0){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            request.login(username, password, new dbRequest.LoginCallback() {
                                @Override
                                public void onSuccess(String id, String username, String email) {
                                    mPBar.setVisibility(View.GONE);
                                    sessionManager.insertUser(id, username, email);
                                    Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(String message) {
                                    mPBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }, 1000);
                }else{
                    mPBar.setVisibility(View.GONE);
                    if(username.length()==0){
                        mUsernameTil.setError("Please write your username.");
                    }
                    if(password.length()==0){
                        mPasswordTil.setError("Please enter your password.");
                    }
                }
            }
        });

    }
}
