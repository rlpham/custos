package com.example.custos;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


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
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ContactsActivity extends Fragment {
    DBHandler db = new DBHandler();





    public ContactsActivity() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.@return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsActivity newInstance() {
        ContactsActivity fragment = new ContactsActivity();
        //    Bundle args = new Bundle();
        //     args.putString(ARG_PARAM1, param1);
        //      args.putString(ARG_PARAM2, param2);
        //  fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //   if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        // }

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


    public void buttonAction(Button button)
    {

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              v.setVisibility(View.GONE);
            }
        });
    }



}



/*
        if (CheckPermission(ContactsActivity.this, permissons[0]))
        {
            outputText = (TextView) findViewById(R.id.contactView);
            fetchContacts();


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sendMsgFab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"));
                    startActivityForResult(sendIntent , 0);

                }
            });




        } else
            {
            // you do not have permission go request runtime permissions
            RequestPermission(ContactsActivity.this, permissons, REQUEST_RUNTIME_PERMISSION);
        }
    }*/
/*




    public void fetchContacts()
    {

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0)
        {

            while (cursor.moveToNext())
            {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                boolean extraspace = false;
                boolean isFound = name.contains("scam") || name.contains("Scam") || name.contains("Spam") || name.contains("spam") || name.contains("Text");
                if(isFound == true)
                {
                    cursor.moveToNext();
                }


                else
                {

                if (hasPhoneNumber > 0)
                {

                    output.append("\n Name:" + name);

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                        int count = 1;

                    String samephoneNumber = "";
                    while (phoneCursor.moveToNext())
                    {

                        if(count >= 1)
                        {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            if(samephoneNumber.equals(phoneNumber))
                            {
                             break;
                            }
                        }

                        phoneNumber = samephoneNumber =  phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number " + count +": " + phoneNumber);
                        count -=- 1;
                        if(count >= 2)
                        {
                            extraspace = true;
                        }

                    }

                    phoneCursor.close();

                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,    null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);


                    //TODO if they have email add it, if not dont show
                    while (emailCursor.moveToNext())
                    {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        if(email.length() == 0)
                        {

                        }
                        else
                            {
                            output.append("\nEmail:" + email);
                        }
                    }

                    emailCursor.close();
                }



                if(extraspace)
                {
                    output.append("\n");
                }
                else
                {
                    extraspace = false;
                }
                }
            }

            outputText.setText(output);
        }
    }



    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults)
    {
        switch (permsRequestCode)
        {

            case REQUEST_RUNTIME_PERMISSION:
                {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // you have permission go ahead
                    outputText = (TextView) findViewById(R.id.contactView);
                    fetchContacts();

                } else
                    {
                    // you do not have permission show toast.
                }
                return;
            }
        }
    }

    public void RequestPermission(Activity thisActivity, String[] Permission, int Code)
    {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Permission[0])
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, Permission[0]))
            {
            }
            else
                {
                ActivityCompat.requestPermissions(thisActivity, Permission, Code);
            }
        }
    }

    public boolean CheckPermission(Context context, String Permission)
    {
        if (ContextCompat.checkSelfPermission(context, Permission) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
            {
            return false;
        }
    }*/





