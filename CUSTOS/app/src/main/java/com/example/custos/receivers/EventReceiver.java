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
        String name = intent.getStringExtra("name");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hello")
                .setSmallIcon(R.drawable.ic_notifications_yellow_60dp)
                .setContentTitle(name)
                .setContentText("Event begins in 15 minutes")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());
    }
}
