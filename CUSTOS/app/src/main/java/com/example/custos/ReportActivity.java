package com.example.custos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportActivity extends AppCompatActivity {

    TextView eve;
    TextView con;
    TextView mem;
    Button share;
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    int eventCounter = 0;
    int contactCounter = 0;
    String allText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_page);

        eve = findViewById(R.id.eventViewNumber);
        con = findViewById(R.id.contactViewNumber);
        mem = findViewById(R.id.memberViewNumber);
        share = findViewById(R.id.shareReport);

        DatabaseReference datta;

        datta = FirebaseDatabase.getInstance().getReference("user_event").child(firebaseUser.getUid());

        datta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Users : dataSnapshot.getChildren()) {
                    //System.out.println(Users.toString());
                    incrementEvent();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        datta = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("contacts");
        datta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Users : dataSnapshot.getChildren()) {
                    //System.out.println(Users.toString());
                    incrementContact();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getAll();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                //emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Custos Report");
                emailIntent.putExtra(Intent.EXTRA_TEXT, allText);

                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                finish();
            }
        });



    }


    public void incrementEvent()
    {
        eventCounter -=- 1;
        eve.setText("Number of Events: " + eventCounter);
    }

    public void incrementContact()
    {
        contactCounter -=- 1;
        con.setText("Number of Contacts: " + contactCounter);
    }

    public void getAll()
    {
        //temporarily
        allText = "Number of Events: " + eventCounter + "\nNumber of Contacts: " + contactCounter + "\n" +mem.getText().toString();
    }


}
