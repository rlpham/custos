
package com.example.custos;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EmergencyContactsActivity extends AppCompatActivity {
    int count = 0;
    private String m_Text = "";
    boolean checkEdit = false;
    public SearchView searchView;
    final String ALPHABET = "123456789abcdefghjkmnpqrstuvwxyz";

    boolean deleting = false;
    boolean editing = false;
    boolean duplicate = false;
    final String ec = "emergency_contacts";
    int checkEC = 0;

    Button exit;



    DatabaseReference datta3;

    public EmergencyContactsActivity() {

    }

    // TODO: Rename and change types and number of parameters
    public static EmergencyContactsActivity newInstance() {
        EmergencyContactsActivity fragment = new EmergencyContactsActivity();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.emergency_contacts);

        ViewGroup layout = (ViewGroup) findViewById(R.id.listContact);

        exit = (Button) findViewById(R.id.button33);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
              EmergencyContactsActivity.this.finish();
            }
        });


        datta3 = FirebaseDatabase.getInstance().getReference("Users").child("jdoe11");

        if(editing == false)
        {


        datta3.addValueEventListener(new ValueEventListener() {
        String info = "";
        String number = "";



            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
            if(!dataSnapshot.hasChild(ec))
            {
            datta3 = FirebaseDatabase.getInstance().getReference("Users").child("jdoe11").child(ec);
            }
            else
            {

                int nameequal = dataSnapshot.toString().indexOf("name=");
                int comma = dataSnapshot.toString().indexOf(", phone_number");


                info = dataSnapshot.toString().substring(nameequal + 5, comma);



                int phonenumberequala = dataSnapshot.toString().indexOf("phone_number=");
                int end = dataSnapshot.toString().indexOf("}}");

                number = dataSnapshot.toString().substring(phonenumberequala + 13, end);

                System.out.println(info);
                System.out.println(number);
                if(count == 0) {
                    generateButton(info + ": +" + number);
                }

            }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        }
        System.out.println("TEST" + "\t\t\t" + count);
        editing = false;



    }





    public void editButton(final Button button, final String name) {
        count++;
        ViewGroup layout = (ViewGroup) findViewById(R.id.listContact);

        AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyContactsActivity.this);
        builder.setTitle(name);
        builder.setCancelable(false);
        editing = true;

        View viewInflated = LayoutInflater.from(EmergencyContactsActivity.this).inflate(R.layout.blank_page, (ViewGroup) layout, false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        final EditText input2 = (EditText) viewInflated.findViewById(R.id.input2);
        builder.setView(viewInflated);

        String titleph = name.substring(name.indexOf('+')+1);


        //     System.out.println(titlename + " testing " + titleph);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String str = input2.getText().toString().replaceAll("[^\\d]", "").trim();

                System.out.println("TESTING PHONE: " + str);

                if (input.getText().toString().trim().length() <= 3) {
                    Toast.makeText(EmergencyContactsActivity.this, "Name is too short", Toast.LENGTH_SHORT).show();
                    //  dialog.dismiss();
                    editButton(button, name);


                }



                else if(str.length() != 10)
                {

                    Toast.makeText(EmergencyContactsActivity.this, "Enter only 10 digits", Toast.LENGTH_SHORT).show();
                    //  dialog.dismiss();

                    editButton(button, name);

                }


                else
                {

                    if (editing) {




                        datta3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                for (DataSnapshot Users : dataSnapshot2.getChildren()) {

                                    int nameequal = Users.toString().indexOf("name=");
                                    int comma = Users.toString().indexOf(", phone_number");
                                    String contact = Users.toString().substring(nameequal + 5);
//                                            contact = contact.substring(0,contact.indexOf(','));
                                    System.out.println(Users.toString());
                                    int keypos = Users.toString().indexOf("key =");
                                    int keystop = Users.toString().indexOf(", value");
                                    String key = Users.toString().substring(keypos + 5,keystop);
                                    key = key.trim();
                                    System.out.println("KEY : " + key);
                                    datta3 = FirebaseDatabase.getInstance().getReference("Users").child("jdoe11").child(ec).child(key);
                                 //   datta3 = FirebaseDatabase.getInstance().getReference("Users").child("rlpham18").child("contacts").child(key); /////////////
                                    String titlename = name.substring(0,name.indexOf(':'));
                                    System.out.println("");
                                    System.out.println(contact + ":" + titlename);
                                    System.out.println("");
                                    if(contact.contains(titlename))
                                    {
                                        datta3 = FirebaseDatabase.getInstance().getReference("Users").child("jdoe11").child(ec).child(key).child("name");

                                        datta3.setValue(input.getText().toString());
                                        datta3 = FirebaseDatabase.getInstance().getReference("Users").child("jdoe11").child(ec).child(key).child("phone_number");
                                        datta3.setValue(str);
                                        break;
                                    }


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }






                    dialog.dismiss();
                    m_Text = input.getText().toString() + ": +" + input2.getText().toString();
                    button.setText(m_Text);
                }





            }
        });

        builder.show();
        datta3 = FirebaseDatabase.getInstance().getReference("Users").child("jdoe11").child(ec);
    }




    public void generateButton(String title) {
        ImageView imageView = new ImageView(EmergencyContactsActivity.this);

        ViewGroup layout = (ViewGroup) findViewById(R.id.contactscroller);
        imageView.setImageResource(R.drawable.line);


        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        final Button btnTag = new Button(EmergencyContactsActivity.this );

        btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnTag.setBackgroundColor(Color.parseColor("#1D1D1D"));
        btnTag.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        btnTag.setText(title);
        btnTag.setTextColor(Color.WHITE);

        buttonAction(btnTag);

        layout.addView(btnTag);


    }




    public void buttonAction(final Button button) {

        int colon = button.getText().toString().indexOf(":");
        final String personName = button.getText().toString();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyContactsActivity.this);
                builder.setCancelable(false);

                builder.setTitle((button.getText().toString()))
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        })
                        .setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                editButton(button,button.getText().toString());


//
//                                String ph = button.getText().toString().replaceAll("[^\\d]", "");
//                                ph = ph.trim();
//
//                                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ph, null)));
                            }
                        })
                        .setNegativeButton("Text", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String ph = button.getText().toString().replaceAll("[^\\d]", "");
                                ph = ph.trim();


                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setData(Uri.parse("sms:"+ph));
                                startActivityForResult(sendIntent , 0);

                            }
                        });



                builder.create();
                builder.show();

            }
        });



    }


}//end
