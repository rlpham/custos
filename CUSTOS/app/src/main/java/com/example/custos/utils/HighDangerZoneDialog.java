package com.example.custos.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.custos.R;

public class HighDangerZoneDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Chill);
        final String highDZCity = getArguments().getString("highDZCity");
        final String timePosted = getArguments().getString("timemarkerposted");
        builder.setTitle("High Danger Zone Added");
        builder.setMessage("New Danger Zone has been added in " + highDZCity + " at " + timePosted)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
