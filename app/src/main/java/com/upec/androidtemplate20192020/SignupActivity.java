package com.upec.androidtemplate20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputLayout;
import com.upec.androidtemplate20192020.dbRequest.dbRequest;

import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private Button mSignupButton;
    private TextInputLayout mUsernameTil, mEmailTil, mPasswordTil, mPassword2Til;
    private ProgressBar mPb_loader;
    private RequestQueue queue;
    private dbRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mSignupButton = (Button) findViewById(R.id.activity_signup_signup_button);
        mPb_loader = (ProgressBar) findViewById(R.id.activity_signup_pb_loader);

        mUsernameTil = (TextInputLayout) findViewById(R.id.activity_signup_username_til);
        mEmailTil = (TextInputLayout) findViewById(R.id.activity_signup_email_til);
        mPasswordTil = (TextInputLayout) findViewById(R.id.activity_signup_password_til);
        mPassword2Til = (TextInputLayout) findViewById(R.id.activity_signup_password2_til);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new dbRequest(this, queue);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPb_loader.setVisibility(View.VISIBLE);
                String username = mUsernameTil.getEditText().getText().toString().trim();
                String email = mEmailTil.getEditText().getText().toString().trim();
                String password = mPasswordTil.getEditText().getText().toString().trim();
                String password2 = mPassword2Til.getEditText().getText().toString().trim();

                if (username.length()>0 && email.length()>0 && password.length()>0 && password2.length()>0) {

                    request.signup(username, email, password, password2, new dbRequest.SignupCallback() {
                        @Override
                        public void onSuccess(String message) {
                            mPb_loader.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("SIGNUP", message);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void inputError(Map<String, String> errors) {
                            mPb_loader.setVisibility(View.GONE);
                            if (errors.get("username") != null){
                                mUsernameTil.setError(errors.get("username"));
                            }else{
                                mUsernameTil.setErrorEnabled(false);
                            }
                            if (errors.get("email") != null){
                                mEmailTil.setError(errors.get("email"));
                            }else{
                                mEmailTil.setErrorEnabled(false);
                            }
                            if (errors.get("password") != null){
                                mPasswordTil.setError(errors.get("password"));
                            }else{
                                mPasswordTil.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            mPb_loader.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    if(username.length()==0){
                        mUsernameTil.setError("Please write a username.");
                    }
                    if(password.length()==0){
                        mPasswordTil.setError("Please write a password.");
                    }
                    if(password2.length()==0){
                        mPassword2Til.setError("Please re-enter your password.");
                    }
                    if(email.length()==0){
                        mEmailTil.setError("Please write an e-mail.");
                    }
                }
            }
        });
    }
}
