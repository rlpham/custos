package com.example.custos.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.custos.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity){
        this.activity = activity;
    }
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

    }
    public void dismissDialog(){
        alertDialog.dismiss();
    }
}
