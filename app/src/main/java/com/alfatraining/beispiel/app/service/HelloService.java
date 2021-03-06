package com.alfatraining.beispiel.app.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.alfatraining.beispiel.app.MainActivity;
import com.alfatraining.beispiel.app.R;
import com.alfatraining.beispiel.app.network.NetworkHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class HelloService extends Service {

    public static final String SERVICE_FINISHED_ACTION = "com.alfatraining.beispiel.finish.action";
    public static final String SERVICE_RESULT = "SERVICE_RESULT";
    private static final String LOG_TAG = HelloService.class.getName();
    private static final int ONGOING_NOTIFICATION_ID = 2;
    private static final String NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID";

    @Override
    public void onCreate() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Notification notification =
                new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(getText(R.string.notification_title))
                        .setContentText(getText(R.string.notification_message))
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentIntent(pendingIntent)
                        .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent broadcastIntent = new Intent(SERVICE_FINISHED_ACTION);
                broadcastIntent.putExtra(SERVICE_RESULT, "result return from service");
                LocalBroadcastManager.getInstance(HelloService.this).sendBroadcast(broadcastIntent);
            }
        }).start();
        //TODO Service Operation
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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