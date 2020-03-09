package com.example.custos;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import afu.org.checkerframework.checker.oigj.qual.O;

public class ContactsActivity extends DialogFragment  {
    DBHandler db = new DBHandler();
    private String m_Text = "";
    boolean checkEdit = false;
    public SearchView searchView;

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

    getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.contactpage, container, false);

        final LinearLayout layout = (LinearLayout) view.findViewById(R.id.contactscroller);
            //LinearLayout
        final Button contactAdder = (Button) view.findViewById(R.id.button33);
            contactAdder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    addContactToLayout(contactAdder,layout);
                }


            });


        try {




            JSONArray name = db.getContacts();
            JSONArray number = db.getNumber();
            ArrayList<String> listShow = new ArrayList<String>();
            ArrayList<String> listShow2 = new ArrayList<String>();
          for(int i= 0;i < db.getContacts().length(); i -=- 1) {
              listShow.add(name.get(i).toString());
              listShow2.add(number.get(i).toString());
          }




            for(int i= 0;i < db.getContacts().length(); i -=- 1) {


                generateButton(listShow.get(i) + ": +" + listShow2.get(i), layout);

            }

        }
        catch (JSONException e){

        }

        // Inflate the layout for this fragment
        return view;
    }





        public void addContactToLayout(final Button button, final LinearLayout layout)
        {







            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Enter Contact Information");
            builder.setCancelable(false);
            //builder.setMessage("test");
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.blank_page, (ViewGroup) getView(), false);

            final EditText input = (EditText) viewInflated.findViewById(R.id.input);
            final EditText input2 = (EditText) viewInflated.findViewById(R.id.input2);
            builder.setView(viewInflated);


            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    if(TextUtils.isEmpty(input.getText().toString()) || input.getText().toString().trim().length() == 0  || TextUtils.isEmpty(input2.getText().toString()) || input2.getText().toString().length() != 10) {
                        Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();
                      //  dialog.dismiss();

                        addContactToLayout(button,layout);

                    }
                    else {

                        dialog.dismiss();



                            m_Text = input.getText().toString() + ": +" + input2.getText().toString();
                           // System.out.println("test");
                            ImageView imageView = new ImageView(layout.getContext());


                            imageView.setImageResource(R.drawable.line);


                            imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                            final Button btnTag = new Button(layout.getContext());

                            btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            btnTag.setBackgroundColor(Color.parseColor("#1B1B1B"));
                            btnTag.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                            btnTag.setText(m_Text);
                            btnTag.setTextColor(Color.WHITE);

                            buttonAction(btnTag);

                            layout.addView(btnTag);





                    }




                }
            });

            builder.show();


        }






    public void generateButton(String title,LinearLayout layout){
        ImageView imageView = new ImageView(layout.getContext());


        imageView.setImageResource(R.drawable.line);


        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        final Button btnTag = new Button(layout.getContext());

        btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnTag.setBackgroundColor(Color.parseColor("#1B1B1B"));
        btnTag.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        btnTag.setText(title);
        btnTag.setTextColor(Color.WHITE);

       buttonAction(btnTag);

       layout.addView(btnTag);


    }








    public void buttonAction(final Button button)
    {

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int colon = button.getText().toString().indexOf(":");
                final String personName = button.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);

                builder.setTitle((button.getText().toString()))
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {


                        }})
                        .setNeutralButton("Delete", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {

                            deleteButton(button);
                        }})
                        .setNegativeButton("Edit", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {

                            editButton(button, personName);

                        }});
                 builder.create();
                 builder.show();

            }
        });

    }


    public void deleteButton(final Button button)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure?");
        builder.setCancelable(false);


        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                button.setVisibility(View.GONE);


            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        builder.show();
    }




    public void editButton(final Button button, final String name)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(name);
        builder.setCancelable(false);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.blank_page, (ViewGroup) getView(), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        final EditText input2 = (EditText) viewInflated.findViewById(R.id.input2);
        builder.setView(viewInflated);


        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if(TextUtils.isEmpty(input.getText().toString()) ||input.getText().toString().trim().length() == 0 || TextUtils.isEmpty(input2.getText().toString()) ||  input2.getText().toString().length() != 10) {
                    Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();

                   editButton(button,name);
                }



                    dialog.dismiss();
                    m_Text = input.getText().toString() + ": +" + input2.getText().toString();
                    button.setText(m_Text);


            }
        });

        builder.show();
    }
}




