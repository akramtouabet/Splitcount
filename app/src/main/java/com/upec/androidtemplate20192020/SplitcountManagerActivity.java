package com.upec.androidtemplate20192020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.upec.androidtemplate20192020.dbRequest.dbRequest;

import java.util.ArrayList;

public class SplitcountManagerActivity extends AppCompatActivity {

    private FloatingActionButton mFabAddExpenditure;
    private int sid;
    private String name, description, sp_code;
    private SessionManager sessionManager;
    private TextInputLayout mAmount;
    private TextView mspCode;
    private dbRequest request;
    private RequestQueue queue;
    private ArrayList<Expense> expenses;
    private BottomAppBar bottomAppBar;





/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.appBarQuit:
                request.leaveSplitcount(String.valueOf(sid), sessionManager.getId(), new dbRequest.LeaveSplitcountCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
                        intent.putExtra("LEAVESUCCESS", message);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(), "HAHA", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splitcount_manager);
        Intent intent = getIntent();

        bottomAppBar = (BottomAppBar) findViewById(R.id.activity_splitcount_manager_bottomactionbar);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                request.leaveSplitcount(String.valueOf(sid), sessionManager.getId(), new dbRequest.LeaveSplitcountCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
                        intent.putExtra("LEAVESUCCESS", message);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });

        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new dbRequest(this, queue);
        sid = intent.getIntExtra("sid", -1);
        


        request.listExpenses(String.valueOf(sid), sessionManager, new dbRequest.ExpensesListCallback() {
            @Override
            public void onSuccess(ArrayList<Expense> expenses) {
                RecyclerView rv = (RecyclerView) findViewById(R.id.rvList2);
                rv.setLayoutManager(new LinearLayoutManager(SplitcountManagerActivity.this));
                rv.setAdapter(new ExpensesAdapter(expenses));
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), "No Expenses found.", Toast.LENGTH_SHORT).show();
            }


        });

    mFabAddExpenditure = (FloatingActionButton) findViewById(R.id.activity_splitcount_manager_add_fab);

        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        sp_code = intent.getStringExtra("sp_code");
        mspCode = (TextView) findViewById(R.id.spcode);
        mspCode.setText("Invite code for this splitcount : " + sp_code);




        mFabAddExpenditure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddExpenditureActivity.class);
                intent.putExtra("sid", sid);
                intent.putExtra("sp_code", sp_code);
                startActivity(intent);
            }
        });
    }
}
