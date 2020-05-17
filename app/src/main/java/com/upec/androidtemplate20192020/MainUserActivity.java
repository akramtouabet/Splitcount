package com.upec.androidtemplate20192020;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.upec.androidtemplate20192020.dbRequest.dbRequest;

import java.util.ArrayList;

public class MainUserActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private dbRequest request;
    private RequestQueue queue;
    private FloatingActionButton fabMain, fabAdd, fabFind;
    private Float translationY = 100f;
    private boolean isOpenMenu = false;
    private OvershootInterpolator interpolator = new OvershootInterpolator();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_main_edit_account:
                Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_main_about:
                Toast.makeText(getApplicationContext(), "Created by : Akram TOUABET & Karim DAHOUMANE", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_main_logout:
                sessionManager.logout();
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openMenu(){
        isOpenMenu = !isOpenMenu;

        fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        fabAdd.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabFind.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeMenu(){
        isOpenMenu = !isOpenMenu;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabAdd.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabFind.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void initFab() {
        fabMain = findViewById(R.id.fab_main);
        fabAdd = findViewById(R.id.fab_add);
        fabFind = findViewById(R.id.fab_find);

        fabFind.setAlpha(0f);
        fabAdd.setAlpha(0f);

        fabAdd.setTranslationY(translationY);
        fabFind.setTranslationY(translationY);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpenMenu)
                    closeMenu();
                else
                    openMenu();
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddSplitcountActivity.class);
                startActivity(intent);
            }
        });
        fabFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinSplitcountActivity.class);
                startActivity(intent);;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        initFab();
        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new dbRequest(this, queue);
        request.listSplitCount(sessionManager, new dbRequest.SplitcountListCallback() {
            @Override
            public void onSuccess(ArrayList<SplitCount> splitcounts) {
                final RecyclerView rv = (RecyclerView) findViewById(R.id.rvList);
                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rv.setAdapter(new MyAdapter(splitcounts));
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), "You aren't part of any splitcount.", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("CREATESUCCESS")){
            Toast.makeText(this, intent.getStringExtra("CREATESUCCESS"), Toast.LENGTH_SHORT).show();
        }else if (intent.hasExtra("JOINSUCCESS")){
            Toast.makeText(this, intent.getStringExtra("JOINSUCCESS"), Toast.LENGTH_SHORT).show();
        }else if (intent.hasExtra("LEAVESUCCESS")){
            Toast.makeText(this, intent.getStringExtra("LEAVESUCCESS"), Toast.LENGTH_SHORT).show();
        }else if (getIntent().hasExtra("EXPENDITURESUCCESS")){
            Toast.makeText(this, intent.getStringExtra("EXPENDITURESUCCESS"), Toast.LENGTH_SHORT).show();
        }


        sessionManager = new SessionManager(this);
        if (sessionManager.isLogged()){
            String username = sessionManager.getUsername();
            String id = sessionManager.getId();
        }
            }
}
