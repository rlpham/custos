package com.example.custos.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.custos.R;
import com.example.custos.SecondSplashActivity;

public class FirstTimeLoginDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Chill);
        builder.setTitle("Important!");
        builder.setMessage("Please set your PIN, address, and others in User Information");
        builder.setIcon(R.drawable.ic_error_yellow_24dp);
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), SecondSplashActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();

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


        return builder.create();
    }
}
