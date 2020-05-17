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

public class JoinSplitcountActivity extends AppCompatActivity {

    private Button mJoinButton;
    private TextInputLayout mCode;
    private ProgressBar mPb_loader;
    private RequestQueue queue;
    private dbRequest request;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_splitcount);

        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new dbRequest(this, queue);
        mPb_loader = (ProgressBar) findViewById(R.id.activity_join_splitcount_pb_loader);
        mCode = (TextInputLayout) findViewById(R.id.activity_join_splitcount_code_til);
        mJoinButton = (Button) findViewById(R.id.activity_join_splitcount_join_button);

        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPb_loader.setVisibility(View.VISIBLE);
                String Code = mCode.getEditText().getText().toString().trim();
                if (Code.length() > 0){
                    request.joinSplitcount(Code, sessionManager, new dbRequest.SplitcountJoinCallback() {
                        @Override
                        public void onSuccess(String message) {
                            mPb_loader.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
                            intent.putExtra("JOINSUCCESS", message);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void inputError(String errors) {
                            mPb_loader.setVisibility(View.GONE);
                            if (errors != null){
                                mCode.setError(errors);
                            }else{
                                mCode.setErrorEnabled(false);
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
                    mCode.setError("Please write a splitcount code !");
                }
            }
        });

    }
}
