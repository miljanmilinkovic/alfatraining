package com.alfatraining.beispiel.app;

import android.content.Intent;
import android.os.Bundle;

import com.alfatraining.beispiel.app.list.ListAdapterAndroidVersions;
import com.alfatraining.beispiel.app.service.HelloService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    static final String URL_STRING = "https://jsonplaceholder.typicode.com/todos/"; // "https://jsonplaceholder.typicode.com/users"

    ListView mListView;
    ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mListView = findViewById(R.id.list_view);
        mListAdapter = new ListAdapterAndroidVersions(this);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start) {
            Intent intent = new Intent(this, HelloService.class);
            intent.putExtra(HelloService.SERVICE_PARAM, URL_STRING);
            startService(intent);

            return true;
        }
        if (id == R.id.action_stop) {
            Intent intent = new Intent(this, HelloService.class);
            stopService(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}