package com.upec.androidtemplate20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputLayout;
import com.upec.androidtemplate20192020.dbRequest.dbRequest;

import java.util.Map;

public class AddSplitcountActivity extends AppCompatActivity {

    private Button mCreateButton;
    private TextInputLayout mSplitcountName, mSplitcountDescription;
    private ProgressBar mPb_loader;
    private RequestQueue queue;
    private dbRequest request;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_splitcount);

        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new dbRequest(this, queue);
        mPb_loader = (ProgressBar) findViewById(R.id.activity_add_splitcount_pb_loader);
        mSplitcountName = (TextInputLayout) findViewById(R.id.activity_add_splitcount_name_til);
        mSplitcountDescription = (TextInputLayout) findViewById(R.id.activity_add_splitcount_description_til);
        mCreateButton = (Button) findViewById(R.id.activity_add_splitcount_add_button);

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPb_loader.setVisibility(View.VISIBLE);
                String splitcountName = mSplitcountName.getEditText().getText().toString().trim();
                String splitcountDescription = mSplitcountDescription.getEditText().getText().toString().trim();
                if (splitcountName.length() > 0){
                    request.createSplitcount(splitcountName, splitcountDescription, sessionManager, new dbRequest.SplitcountCreateCallback() {
                        @Override
                        public void onSuccess(String message) {
                            mPb_loader.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
                            intent.putExtra("CREATESUCCESS", message);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void inputError(Map<String, String> errors) {
                            mPb_loader.setVisibility(View.GONE);
                            if (errors.get("name") != null){
                                mPb_loader.setVisibility(View.GONE);
                                mSplitcountName.setError(errors.get("name"));
                            }else{
                                mSplitcountName.setErrorEnabled(false);
                            }
                            if (errors.get("query") != null){
                                mPb_loader.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), errors.get("query"), Toast.LENGTH_SHORT).show();
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
                    mSplitcountName.setError("Please write a splitcount name.");
                }
            }
        });
    }
}
