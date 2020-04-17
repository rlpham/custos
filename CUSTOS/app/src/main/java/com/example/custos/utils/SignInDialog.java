package com.example.custos.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.example.custos.R;

public class SignInDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    public SignInDialog(Activity activity){
        this.activity = activity;
    }
    public void startDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.custom_dialog_signin,null));
        builder.setCancelable(false);
        alertDialog = builder.create();

        // Set alertDialog "not focusable" so nav bar still hiding:
        alertDialog.getWindow().
                setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // Set full-sreen mode (immersive sticky):
        alertDialog.getWindow().getDecorView().setSystemUiVisibility(Common.ui_flags);

        // Show the alertDialog:
        alertDialog.show();

        // Set dialog focusable so we can avoid touching outside:
        alertDialog.getWindow().
                clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        alertDialog.show();

    }
    public void dismissDialog(){
        alertDialog.dismiss();
    }
}
