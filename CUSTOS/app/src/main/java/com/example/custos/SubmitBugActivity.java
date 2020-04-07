package com.example.custos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custos.utils.Common;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SubmitBugActivity extends AppCompatActivity {

    EditText et;
    Button submit;
    TextView cLeft;
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference datta;
    int counter = 0;
    ProgressDialog mProgressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_bug);



        datta = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION).child(firebaseUser.getUid()).child("userEmail");
        datta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String b = "";
                for (DataSnapshot Users : dataSnapshot.getChildren()) {
                    System.out.println(Users.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });





        submit = findViewById(R.id.shareReport);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {









                String total = et.getText().toString();

                PackageManager pm = SubmitBugActivity.this.getPackageManager();
                PackageInfo packageInfo = null;
                try {
                    packageInfo = pm.getPackageInfo("com.example.custos", PackageManager.GET_PERMISSIONS);
                } catch (PackageManager.NameNotFoundException e) {

                    e.printStackTrace();
                }



                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                Date updateTime = new Date(packageInfo.lastUpdateTime);
                String submitTime = updateTime.toString();

                String version = packageInfo.versionName;
                if(counter == 0)
                {

                    sendMail(total);
                    counter++;
                }

                final String tot = "Custos version: " + version + "\n" + submitTime + "\n" + total;
                AlertDialog.Builder builder = new AlertDialog.Builder(SubmitBugActivity.this);
                builder.setCancelable(false);
                builder.setMessage("Thank you for your report");
                builder.setTitle("Bug Report")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendMail(tot);
                                mProgressDialog = ProgressDialog.show(SubmitBugActivity.this,"Sending message", "Please wait...",false,false);

                                final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            handler.postDelayed(this, 2500);
                                            mProgressDialog.dismiss();
                                        }
                                    }, 4000);


                            }
                        });




                builder.create();
                builder.show();



            }
        });

        cLeft = findViewById(R.id.charactersLeft);
        et = findViewById(R.id.textBoxBug);


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
     checkBox();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }

    public void checkBox()
    {
        int total = 280;
        int userInput = et.getText().toString().length();

        total -= userInput;

        cLeft.setText(total + " Characters Left");
    }

    private void sendMail(String total) {

        
        //TODO get user email and fix this
        String mail = "psucustos@gmail.com";
        String subject = "Bug Report";



        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,total);
        javaMailAPI.execute();

    }




}
