package com.upec.androidtemplate20192020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.upec.androidtemplate20192020.dbRequest.dbRequest;

import java.util.ArrayList;

public class BalanceActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private dbRequest request;
    private RequestQueue queue;
    private int sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);


        Intent intent = getIntent();
        sid = intent.getIntExtra("sid", -1);
        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new dbRequest(this, queue);

        request.listDebts(String.valueOf(sid), sessionManager, new dbRequest.DebtListCallback(){
            @Override
            public void onSuccess(ArrayList<Balance> balances) {
                RecyclerView rv = (RecyclerView) findViewById(R.id.rvBalanceList);
                rv.setLayoutManager(new LinearLayoutManager(BalanceActivity.this));
                rv.setAdapter(new DebtAdapter(balances));
            }

            @Override
            public void onError(String message) {

            }
        });

    }
}
