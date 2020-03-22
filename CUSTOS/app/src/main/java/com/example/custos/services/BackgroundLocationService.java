package com.example.custos.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

public class BackgroundLocationService extends JobIntentService {

    

    @Override
    public void onCreate() {

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

    }
}
