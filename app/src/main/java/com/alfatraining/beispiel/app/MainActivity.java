package com.alfatraining.beispiel.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.alfatraining.beispiel.app.list.ListAdapterAndroidVersions;
import com.alfatraining.beispiel.app.receiver.StartServiceReceiver;
import com.alfatraining.beispiel.app.service.HelloService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    static final String URL_STRING = "https://jsonplaceholder.typicode.com/todos/"; // "https://jsonplaceholder.typicode.com/users"

    ListView mListView;
    BaseAdapter mListAdapter;

    HelloService mService;
    boolean mBound = false;
    BroadcastReceiver mReceiver;
    BroadcastReceiver mRefreshReceiver;

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
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        mReceiver = new StartServiceReceiver();
        this.registerReceiver(mReceiver, intentFilter);

        mRefreshReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "result from service: " + intent.getStringExtra(HelloService.SERVICE_RESULT), Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter refreshIntentFilter = new IntentFilter(HelloService.SERVICE_FINISHED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mRefreshReceiver, refreshIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRefreshReceiver);
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

            Intent intent = new Intent(this, HelloService.class);
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