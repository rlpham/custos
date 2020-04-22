package com.example.custos.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.custos.R;

public class EventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("test");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hello")
                .setSmallIcon(R.drawable.ic_notifications_yellow_60dp)
                .setContentTitle("Rahul smells")
                .setContentText("Your event begins in 15 minutes")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());
    }
}
