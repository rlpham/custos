package com.example.custos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

public class SettingsActivity extends Fragment {
    public SettingsActivity() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.@return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsActivity newInstance() {
        SettingsActivity fragment = new SettingsActivity();
        //    Bundle args = new Bundle();
        //     args.putString(ARG_PARAM1, param1);
        //      args.putString(ARG_PARAM2, param2);
        //  fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.settings, container, false);
//        LinearLayout layout = (LinearLayout) view.findViewById(R.id.settingscontainer);
        Button Signout=view.findViewById(R.id.settingsSignOut);
        Signout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SecondSplashActivity.class);
                startActivity(intent);
            }
        });
        Button Report = view.findViewById(R.id.SettingReport);
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReportActivity.class);
                startActivity(intent);
            }
        });



        Button bug = view.findViewById(R.id.reportBug);
        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SubmitBugActivity.class);
                startActivity(intent);
            }
        });


        //placeholder atm
        Switch sw = view.findViewById(R.id.darkMode);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
               //     AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else
                {
                 //   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                }



            }
        });


//        Button lightNDark = view.findViewById(R.id.mapMode);
//        lightNDark.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //for light and dark mode
//            }
//        });



        // Inflate the layout for this fragment
        return view;
    }
}
