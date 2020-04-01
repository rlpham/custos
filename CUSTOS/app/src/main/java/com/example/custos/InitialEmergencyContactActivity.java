package com.example.custos;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InitialEmergencyContactActivity extends AppCompatActivity {

    DatabaseReference datta;
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    EditText name;
    EditText phone;
    Button submit;
    final String ALPHABET = "123456789abcdefghjkmnpqrstuvwxyz";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_emergency_contact);
        name = findViewById(R.id.name_input4);
        phone = findViewById(R.id.phone_input4);
        submit = findViewById(R.id.button33);
        final String ec = "emergency_contacts";


        //////////////




        InitialEmergencyContactActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final String usernamed = getRandomWord(20);

        //temporary till someone can figure out how to get right user
        datta = FirebaseDatabase.getInstance().getReference("Users");

        datta.orderByKey()
                .equalTo(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            //uid not exist

                            if (!dataSnapshot.child(firebaseUser.getUid()).exists()) {


                                datta = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child(ec).child(usernamed);
                                //datta.child("name").setValue("donotdeletethis");
                                //datta.child("phone_number").setValue("donotdeletethis");

                            }
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                        ////////////
        datta = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child(ec).child(usernamed);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(namecheck(name) && phonecheck(phone))
                {
                    //datta = FirebaseDatabase.getInstance().getReference("Users").child("userTest").child(ec);
                    datta = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child(ec);
                    datta.child("name").setValue(name.getText().toString());
                    datta.child("phone_number").setValue(phone.getText().toString());
                    Intent intent = new Intent(InitialEmergencyContactActivity.this, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    InitialEmergencyContactActivity.this.finish();
                }
                else
                {
                    Toast.makeText(InitialEmergencyContactActivity.this , "Please enter valid inputs", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public boolean namecheck(EditText name)
    {
       String n = name.getText().toString();


       if(n.trim().length() <= 3)
       {
           return false;
       }
        return true;

    }


    public boolean phonecheck(EditText phone)
    {
        String p = phone.getText().toString();
        if(p.length() != 10)
        {
            return false;
        }
        return true;
    }

                    String getRandomWord(int length) {
                        String r = "";
                        for (int i = 0; i < length; i++) {
                            r += (char) (Math.random() * 26 + 97);
                        }
                        return r;
                    }


}
