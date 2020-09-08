package com.alfatraining.beispiel.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alfatraining.beispiel.app.service.HelloService;

public class StartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "StartServiceReceiver", Toast.LENGTH_LONG).show();
        Intent intentService = new Intent(context, HelloService.class);
        context.startService(intentService);
    }
}