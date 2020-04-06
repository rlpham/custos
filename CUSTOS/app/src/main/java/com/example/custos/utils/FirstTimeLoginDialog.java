package com.example.custos.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.custos.R;
import com.example.custos.SecondSplashActivity;

public class FirstTimeLoginDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.Chill);
        alertDialog.setTitle("Important!");
        alertDialog.setMessage("Please set your PIN, address, and others in User Information");
        alertDialog.setIcon(R.drawable.ic_error_yellow_24dp);
        alertDialog.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), SecondSplashActivity.class);
                startActivity(intent);
            }
        });
        return alertDialog.create();
    }
}
