package com.alfatraining.beispiel.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alfatraining.beispiel.app.network.NetworkHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelloService extends Service {

    public static final String SERVICE_PARAM = "ServiceParam";
    private static final String LOG_TAG = HelloService.class.getName();

    private ServiceHandler serviceHandler;

    // Handler für Messages auf Worker Thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            Log.i(LOG_TAG, msg.getData().getString(SERVICE_PARAM));
            String result = loadUrl(msg.getData().getString(SERVICE_PARAM));
            Log.i(LOG_TAG, result);

            //TODO JSON verabreiten

            //stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Worker Thread starten, weil Service Main Thread benutzt
        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();

        serviceHandler = new ServiceHandler(thread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String param = intent.getStringExtra(SERVICE_PARAM);

        Toast.makeText(this, "service starting " + param, Toast.LENGTH_SHORT).show();

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.setData(intent.getExtras());
        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Keine Binding
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    private String loadUrl(String urlString) {
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