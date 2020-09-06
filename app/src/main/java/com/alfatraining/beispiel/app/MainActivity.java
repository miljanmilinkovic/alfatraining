package com.alfatraining.beispiel.app;

import android.os.Bundle;

import com.alfatraining.beispiel.app.list.ListAdapterAndroidVersions;
import com.alfatraining.beispiel.app.network.NetworkHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    static final String LOG_TAG = MainActivity.class.getName();
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

        // new Handler() -> main thread!!
        HandlerThread handlerThread = new HandlerThread("worker");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "URL1 Laden startet ", Toast.LENGTH_LONG).show();
                    }
                });

                final String result = loadUrl();
                //TODO Json verarbeiten und Adapter zuweisen

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "URL1 geladen " + result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, 1000);

        handler.post(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "URL2 Laden startet ", Toast.LENGTH_LONG).show();
                    }
                });

                final String result = loadUrl();
                //TODO Json verarbeiten und Adapter zuweisen

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "URL2 geladen " + result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String loadUrl() {
        try {
            InputStream inputStream = NetworkHelper.getHttpResponse(URL_STRING);

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                    sb.append(line).append("\n");

                return sb.toString();
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }
}