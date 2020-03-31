package com.example.custos;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import org.w3c.dom.Text;

public class ConfirmSafetyActivity extends AppCompatActivity {


    String pinmsg = "";
    Button bt1;
    Button bt2;
    Button bt3;
    Button bt4;
    Button bt5;
    Button bt6;
    Button bt7;
    Button bt8;
    Button bt9;
    Button bt0;
    Button btpindelete;
    TextView pinView;
    TextView initialSafetyMessage;
    Button confirmButton;
    boolean same = false;
    int counter = 0;
    String pinmsg2 = "";
    TextView pinView2;
    Button redo;
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference datta;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.initial_safety_pin);
        bt1 = findViewById(R.id.btn1);
        bt2 = findViewById(R.id.btn2);
        bt3 = findViewById(R.id.btn3);
        bt4 = findViewById(R.id.btn4);
        bt5 = findViewById(R.id.btn5);
        bt6 = findViewById(R.id.btn6);
        confirmButton = findViewById(R.id.confirmSafetyButton);
        bt7 = findViewById(R.id.btn7);
        bt8 = findViewById(R.id.btn8);
        bt9 = findViewById(R.id.btn9);
        bt0 = findViewById(R.id.btn0);
        btpindelete = findViewById(R.id.deletesafetypin);
        pinView = findViewById(R.id.pinmessage);
        redo = findViewById(R.id.redoPin);


        initialSafetyMessage = findViewById(R.id.safety_pin_msg);
        initialSafetyMessage.setText("Enter safety pin");
        pinView2 = findViewById(R.id.pinmessage2);

    }

    public void deleteNumber(View button)
    {
        initialSafetyMessage.setText("Enter safety pin");
        confirmButton.setVisibility(View.INVISIBLE);

        if(pinmsg.length() != 0)
        {
            pinmsg = pinmsg.substring(0, pinmsg.length() - 1);
            pinView.setText(pinmsg);

        }

        if(pinmsg2.length() != 0)
        {
            pinmsg2 = pinmsg2.substring(0, pinmsg2.length() - 1);
            pinView2.setText(pinmsg2);
        }

    }

    public void confirm(final View view)
    {


        initialSafetyMessage.setText("Enter safety pin");
        pinView.setVisibility(View.INVISIBLE);
        confirmButton.setVisibility(View.INVISIBLE);



        datta =  FirebaseDatabase.getInstance().getReference("Users");
        datta.orderByKey()
                .equalTo(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            //uid not exist

                            if (!dataSnapshot.child(firebaseUser.getUid()).exists()) {


                                datta = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("safetypin");

                            }
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        datta =  FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("safetypin");
        datta.addListenerForSingleValueEvent(new ValueEventListener() {
            final String test = pinView.getText().toString();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue() + "";
                System.out.println(value);

                System.out.println(test);




                if(test.equals(value))
                {
                    Toast.makeText(ConfirmSafetyActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    changePage(view);
                }
                else
                {
                    same = false;
                    pinView.setVisibility(View.VISIBLE);
                    pinmsg = "";
                    pinView.setText(pinmsg);
                    initialSafetyMessage.setText("Try again");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    public void doRedo(View view)
    {

    }


    public void changePage(View view)
    {

        Intent signupIntent = new Intent(view.getContext(), MapsActivity.class);
        startActivity(signupIntent);
    }



    public void buttonClick(View button)
    {


        if(pinmsg.length() == 3)
        {

            confirmButton.setVisibility(View.VISIBLE);
        }



            switch (button.getId())
            {
                case R.id.btn1:
                    if (pinmsg.length() == 4)
                    {
                        break;
                    }

                    pinmsg += "1";
                    pinView.setText(pinmsg);
                    break;

                case R.id.btn2:
                    if (pinmsg.length() == 4)
                    {
                        break;
                    }
                    pinmsg += "2";
                    pinView.setText(pinmsg);
                    break;

                case R.id.btn3:
                    if (pinmsg.length() == 4)
                    {
                        break;
                    }
                    pinmsg += "3";
                    pinView.setText(pinmsg);
                    break;

                case R.id.btn4:
                    if (pinmsg.length() == 4)
                    {
                        break;
                    }
                    pinmsg += "4";
                    pinView.setText(pinmsg);
                    break;

                case R.id.btn5:
                    if (pinmsg.length() == 4)
                    {
                        break;
                    }
                    pinmsg += "5";
                    pinView.setText(pinmsg);
                    break;

                case R.id.btn6:
                    if (pinmsg.length() == 4)
                    {
                        break;
                    }
                    pinmsg += "6";
                    pinView.setText(pinmsg);
                    break;

                case R.id.btn7:
                    if (pinmsg.length() == 4)
                    {
                        break;
                    }
                    pinmsg += "7";
                    pinView.setText(pinmsg);
                    break;

                case R.id.btn8:
                    if (pinmsg.length() == 4)
                    {
                        break;
                    }
                    pinmsg += "8";
                    pinView.setText(pinmsg);
                    break;


                case R.id.btn9:
                    if (pinmsg.length() == 4)
                    {
                        break;
                    }
                    pinmsg += "9";
                    pinView.setText(pinmsg);
                    break;

                case R.id.btn0:
                    if (pinmsg.length() == 4)
                    {
                        break;
                    }
                    pinmsg += "0";
                    pinView.setText(pinmsg);
                    break;



        }

        if(pinmsg.length() >= 1)
        {
            initialSafetyMessage.setText("Enter safety pin");
        }


    }






}
