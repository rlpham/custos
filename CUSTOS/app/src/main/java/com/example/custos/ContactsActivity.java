package com.example.custos;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.Random;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import afu.org.checkerframework.checker.oigj.qual.O;

public class ContactsActivity extends DialogFragment  {
    DBHandler db = new DBHandler();



    public ContactsActivity() {

    }

    // TODO: Rename and change types and number of parameters
    public static ContactsActivity newInstance() {
        ContactsActivity fragment = new ContactsActivity();

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.contactpage, container, false);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.contactscroller);







        try {
            JSONArray name = db.getContacts();

            ArrayList<String> listShow = new ArrayList<String>();
          for(int i= 0;i < db.getContacts().length(); i -=- 1) {
              listShow.add(name.get(i).toString());

          }
            Collections.sort(listShow);

            for(int i= 0;i < db.getContacts().length(); i -=- 1) {






                generateButton(listShow.get(i), layout);


            }

        }
        catch (JSONException e){

        }

        // Inflate the layout for this fragment
        return view;
    }


    public void removeName(View view)
    {
       view.setVisibility(View.GONE);
    }


    public void generateButton(String title,LinearLayout layout){
        ImageView imageView = new ImageView(layout.getContext());

//setting image resource
        imageView.setImageResource(R.drawable.line);

//setting image position
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

//adding view to layout
        layout.addView(imageView);

        //set the properties for button
        final Button btnTag = new Button(layout.getContext());
        btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        // btnTag.setPaddingRelative(0,100,200,500);
        // btnTag.setLeftTopRightBottom(100, 100, 100);
        btnTag.setBackgroundColor(Color.parseColor("#1B1B1B"));
        btnTag.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        btnTag.setText(title);
        btnTag.setTextColor(Color.WHITE);

       buttonAction(btnTag);



       // buttonAction(btnTag,eventID);

        //add button to the layout
        layout.addView(btnTag);


    }


    public void buttonAction(final Button button)
    {



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println(button.getText().toString());


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle((button.getText().toString()))
                        .setPositiveButton("DONE", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {

                            ///
                        }})
                        .setNegativeButton("SETUP", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {


                            ///
                        }});
                 builder.create()   ;
                 builder.show();



            }
        });

    }





}




