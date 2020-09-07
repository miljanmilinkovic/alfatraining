package com.alfatraining.beispiel.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.alfatraining.beispiel.app.list.ListAdapterAndroidVersions;
import com.alfatraining.beispiel.app.service.HelloService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.IBinder;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    static final String URL_STRING = "https://jsonplaceholder.typicode.com/todos/"; // "https://jsonplaceholder.typicode.com/users"

    ListView mListView;
    ListAdapter mListAdapter;

    HelloService mService;
    boolean mBound = false;

    /** Callbacks für bindService() */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // Bind fertig, IBinder bereit
            HelloService.HelloBinder binder = (HelloService.HelloBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


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
        int id = item.getItemId();

        if (id == R.id.action_start) {

            if (!mBound) {
                Intent intent = new Intent(this, HelloService.class);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
            if (mBound) {
                // HelloService Methode
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String result = mService.loadUrl(URL_STRING);
                        Log.i(LOG_TAG, result);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "result: " + result, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).start();
            }

            return true;
        }
        if (id == R.id.action_stop) {
            unbindService(connection);
            mBound = false;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}