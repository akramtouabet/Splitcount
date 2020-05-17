package com.upec.androidtemplate20192020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputLayout;
import com.upec.androidtemplate20192020.dbRequest.dbRequest;

import java.util.ArrayList;

public class AddExpenditureActivity extends AppCompatActivity {

    private int sid, uid;
    private Button mAddButton;
    private TextInputLayout mAmount, mTitle;
    private ProgressBar mPb_loader;
    private SessionManager sessionManager;
    private RequestQueue queue;
    private dbRequest request;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenditure);
        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new dbRequest(this, queue);
        final String uid = sessionManager.getId();
        final int sid = getIntent().getIntExtra("sid", -1);
        final String sp_code = getIntent().getStringExtra("sp_code");



        mAddButton = findViewById(R.id.activity_add_expenditure_add_button);
        mAmount = (TextInputLayout) findViewById(R.id.activity_add_expenditure_amount_til);
        mTitle = (TextInputLayout) findViewById(R.id.activity_add_expenditure_title_til);
        mPb_loader = (ProgressBar) findViewById(R.id.activity_add_expenditure_pb_loader);





        request.spUsersList(String.valueOf(sid), new dbRequest.spUsersListCallback() {
            @Override
            public void onSuccess(ArrayList<User> spUsers) {
                ll = (LinearLayout) findViewById(R.id.usrLayout);
                for (int i=0; i<spUsers.size(); i++){
                    CheckBox cb = new CheckBox(getApplicationContext());
                    cb.setText(spUsers.get(i).getUsername());
                    cb.setChecked(true);
                    ll.addView(cb);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });



        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mPb_loader.setVisibility(View.VISIBLE);
            double amount = Double.parseDouble(mAmount.getEditText().getText().toString().trim());
            String title = mTitle.getEditText().getText().toString().trim();
            if (amount > 0 && title.length() > 0){
                request.addExpenditure(title, sid, uid, amount, new dbRequest.AddExpenditureCallback() {
                    @Override
                    public void onSuccess(String message) {
                        mPb_loader.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), SplitcountManagerActivity.class);
                        intent.putExtra("EXPENDITURESUCCESS", message);
                        intent.putExtra("sid", sid);
                        intent.putExtra("sp_code", sp_code);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        mPb_loader.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                mPb_loader.setVisibility(View.GONE);
                if (title.length()==0)
                    mTitle.setError("Please write a title.");
                if (amount <= 0)
                    mAmount.setError("Please write a valid amount.");
            }


            }
        });
    }
}
