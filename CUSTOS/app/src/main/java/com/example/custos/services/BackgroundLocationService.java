package com.example.custos.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

public class BackgroundLocationService extends JobIntentService {

    //https://androidwave.com/working-with-jobintentservice/
    private static final int JOB_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        for(int i = 0; i < 1000; i++) {
            try {
                System.out.println("Background Service " + i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, BackgroundLocationService.class, JOB_ID, intent);
    }
}
