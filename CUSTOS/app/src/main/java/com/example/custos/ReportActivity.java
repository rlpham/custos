package com.example.custos;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    TextView eve;
    TextView con;
    TextView mem;
    TextView use;
    Button share;
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String allText = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_page);

        eve = findViewById(R.id.eventViewNumber);
        con = findViewById(R.id.contactViewNumber);
        mem = findViewById(R.id.memberViewNumber);
        use = findViewById(R.id.userViewNumber);
        share = findViewById(R.id.shareReport);

        DatabaseReference datta;

//        PackageManager pm = ReportActivity.this.getPackageManager();
//        try {
//            ApplicationInfo appInfo = pm.getApplicationInfo("com.example.custos", 0);
//            String appFile = appInfo.sourceDir;
//            long installed = new File(appFile).lastModified();
//            System.out.println(installed);
//        }
//        catch (Exception E)
//        {
//
//        }


        PackageManager pm = ReportActivity.this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo("com.example.custos", PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Date installTime = new Date( packageInfo.firstInstallTime );
        //System.out.println( "Installed: " + installTime.toString());

        Date updateTime = new Date( packageInfo.lastUpdateTime );
        //System.out.println("Updated: " + updateTime.toString());

        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


       // SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

        ////op code
        String installTimed = dateFormat.format( new Date( packageInfo.firstInstallTime ) );
        System.out.println( "Installed: " + installTimed);
        String initialDay = "Member Since: " + installTimed;
        ////


        String updateTimed = dateFormat.format( new Date( packageInfo.lastUpdateTime ) );
        System.out.println("Updated: " + updateTimed);






        datta = FirebaseDatabase.getInstance().getReference("user_event").child(firebaseUser.getUid());

        datta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot Users : dataSnapshot.getChildren()) {
                        count -=- 1;
                }
                eve.setText("Number of Events: " + count);

                allText += eve.getText().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        datta = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("contacts");
        datta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot Users : dataSnapshot.getChildren()) {
                    count -=- 1;
                }
                con.setText("Number of Contacts: " + count);

                allText +=  "\n" + con.getText().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mem.setText(initialDay);
//        System.out.println(mem.getText().toString() + "TESTINGdlfksodjfodsf");
        //TODO try to get more ideas for report or try to figure out how long theyve been using Custos
        allText += "\n" + initialDay;





          datta = FirebaseDatabase.getInstance().getReference("User Information");

        datta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot Users : dataSnapshot.getChildren()) {
                        count -=- 1;
                }
                use.setText("Number of Users: " + count);

                allText += use.getText().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







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
                //finish();
            }
        });





    }


}
