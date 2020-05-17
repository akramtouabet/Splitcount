package com.upec.androidtemplate20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputLayout;
import com.upec.androidtemplate20192020.dbRequest.dbRequest;

import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {

    private Button mUpdateButtonKeep, mUpdateButtonChange;
    private TextInputLayout mUsernameTil, mEmailTil, mPasswordTil, mPassword2Til, mConfirmPassword;
    private RadioGroup mRadioGroup;
    private ProgressBar mPb_loader;
    private RequestQueue queue;
    private dbRequest request;
    private SessionManager sessionManager;

    public void clearInputs(){
        mPasswordTil.getEditText().getText().clear();
        mPassword2Til.getEditText().getText().clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mUpdateButtonKeep = (Button) findViewById(R.id.activity_update_profile_update_button);
        mUpdateButtonChange = (Button) findViewById(R.id.activity_update_profile_update2_button);

        mPb_loader = (ProgressBar) findViewById(R.id.activity_update_profile_pb_loader);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_password);
        mUsernameTil = (TextInputLayout) findViewById(R.id.activity_update_profile_username_til);
        mEmailTil = (TextInputLayout) findViewById(R.id.activity_update_profile_email_til);

        mPasswordTil = (TextInputLayout) findViewById(R.id.activity_update_profile_password_til);
        mPassword2Til = (TextInputLayout) findViewById(R.id.activity_update_profile_password2_til);
        mConfirmPassword = (TextInputLayout) findViewById(R.id.activity_update_profile_confirm_password_til);


        sessionManager = new SessionManager(this);
        if (sessionManager.isLogged()){
            String username = sessionManager.getUsername();
            String email = sessionManager.getEmail();
            mUsernameTil.getEditText().setText(username);
            mEmailTil.getEditText().setText(email);
        }else{
            Toast.makeText(getApplicationContext(), "FATAL ERROR !", Toast.LENGTH_SHORT).show();
        }

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new dbRequest(this, queue);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if(checkedId==R.id.radio_change_password)
                {
                    mPasswordTil.setVisibility(View.VISIBLE);
                    mPassword2Til.setVisibility(View.VISIBLE);
                    mUpdateButtonKeep.setVisibility(View.GONE);
                    mUpdateButtonChange.setVisibility(View.VISIBLE);
                    clearInputs();
                    mUpdateButtonChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPb_loader.setVisibility(View.VISIBLE);
                            String username = mUsernameTil.getEditText().getText().toString().trim();
                            String email = mEmailTil.getEditText().getText().toString().trim();
                            String passwordConfirm = mConfirmPassword.getEditText().getText().toString().trim();
                            String password = mPasswordTil.getEditText().getText().toString().trim();
                            String password2 = mPassword2Til.getEditText().getText().toString().trim();

                            if (username.length()>0 && email.length()>0 && passwordConfirm.length()>0) {

                                request.updateChange(username, email,password, password2, passwordConfirm, sessionManager, new dbRequest.UpdateProfileChangeCallback() {
                                    @Override
                                    public void onSuccess(String message) {
                                        mPb_loader.setVisibility(View.GONE);
                                        sessionManager.logout();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.putExtra("UPDATESUCCESS", message);
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
                                        if (errors.get("passwordConfirm") != null){
                                            mConfirmPassword.setError(errors.get("passwordConfirm"));
                                        }else{
                                            mConfirmPassword.setErrorEnabled(false);
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
                                mPb_loader.setVisibility(View.GONE);
                                if(username.length() == 0){
                                    mUsernameTil.setError("Please write a username.");
                                }
                                if(email.length()==0){
                                    mEmailTil.setError("Please write an email.");
                                }
                                if(password2.length()==0){
                                    mPassword2Til.setError("Please confirm your password.");
                                }
                                if(password.length()==0){
                                    mPasswordTil.setError("Please write a new password.");
                                }
                                if(passwordConfirm.length()==0){
                                    mConfirmPassword.setError("Please write your old password.");
                                }
                            }
                        }
                    });
                }
                else if(checkedId==R.id.radio_keep_password)
                {
                    mPasswordTil.setVisibility(View.GONE);
                    mPassword2Til.setVisibility(View.GONE);
                    mUpdateButtonKeep.setVisibility(View.VISIBLE);
                    mUpdateButtonChange.setVisibility(View.GONE);
                    clearInputs();
                    mUpdateButtonKeep.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPb_loader.setVisibility(View.VISIBLE);
                            String username = mUsernameTil.getEditText().getText().toString().trim();
                            String email = mEmailTil.getEditText().getText().toString().trim();
                            String password = mConfirmPassword.getEditText().getText().toString().trim();

                            if (username.length()>0 && email.length()>0 && password.length()>0) {
                                request.updateKeep(username, email, password, sessionManager, new dbRequest.UpdateProfileKeepCallback() {
                                    @Override
                                    public void onSuccess(String message) {
                                        mPb_loader.setVisibility(View.GONE);
                                        sessionManager.logout();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.putExtra("UPDATESUCCESS", message);
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
                                            mConfirmPassword.setError(errors.get("password"));
                                        }else{
                                            mConfirmPassword.setErrorEnabled(false);
                                        }
                                    }

                                    @Override
                                    public void onError(String message) {
                                        mPb_loader.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                mPb_loader.setVisibility(View.GONE);
                                if(username.length() == 0){
                                    mUsernameTil.setError("Please write a username.");
                                }
                                if(email.length()==0){
                                    mEmailTil.setError("Please write an email.");
                                }
                                if(password.length()==0){
                                    mConfirmPassword.setError("Please write a new password.");
                                }
                            }
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(), "FATAL ERROR !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
