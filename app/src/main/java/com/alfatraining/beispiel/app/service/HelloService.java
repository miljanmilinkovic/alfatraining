package com.alfatraining.beispiel.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.alfatraining.beispiel.app.network.NetworkHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelloService extends Service {

    private static final String LOG_TAG = HelloService.class.getName();

    // Binder für Klienten
    private final IBinder binder = new HelloBinder();

    /**
     * Klient Binder Klasse
     */
    public class HelloBinder extends Binder {
        public HelloService getService() {
            // Klienten können Service benutzen
            return HelloService.this;
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    public String loadUrl(String urlString) {
        try {
            InputStream inputStream = NetworkHelper.getHttpResponse(urlString);

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