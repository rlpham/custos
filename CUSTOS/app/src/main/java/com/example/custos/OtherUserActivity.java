package com.example.custos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.custos.utils.Common;
import com.example.custos.utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserActivity extends AppCompatActivity {
    public static final String TAG = "OtherUserActivity";
    CircleImageView otherUserImage;
    TextView otherName,
            otherName2,
            otherEmail,
            otherEmail2,
            otherEmergencyName,
            otherPhone,
            backButton;
    Button addFriend;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_activity);
        otherUserImage = findViewById(R.id.imageViewOtherUser);
        otherName = findViewById(R.id.textOtherUserName);
        otherName2 = findViewById(R.id.textOtherUserName2);
        otherEmail = findViewById(R.id.textOtherUserEmail);
        otherEmail2 = findViewById(R.id.textOtherUserEmail2);
        otherEmergencyName = findViewById(R.id.emergencyContactDisplayOtherUser);
        otherPhone = findViewById(R.id.textOtherUserPhoneNumDisplay);
        backButton = findViewById(R.id.back_button_otherUser);
        addFriend = findViewById(R.id.sendRequest);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog();

            }
        });

        intent = getIntent();

        String otherUserId = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION).child(otherUserId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getImageURL().equals("default")){
                    otherUserImage.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(otherUserImage);
                }
                if(dataSnapshot.child(Common.USER_NAME).exists()
                        && !dataSnapshot.child(Common.USER_NAME).getValue().toString().equals("")){
                    String name = dataSnapshot.child(Common.USER_NAME).getValue().toString();
                    otherName.setText(name);
                }else{
                    otherName.setText("User didnt set name");
                }
                if(dataSnapshot.child(Common.USER_NAME).exists()
                        && !dataSnapshot.child(Common.USER_NAME).getValue().toString().equals("")){
                    String name2 = dataSnapshot.child(Common.USER_NAME).getValue().toString();
                    otherName2.setText(name2);
                }else{
                    otherName2.setText("User didnt set name");
                }
                if(dataSnapshot.child(Common.USER_EMAIL).exists()
                        && !dataSnapshot.child(Common.USER_EMAIL).getValue().toString().equals("")){
                    String email = dataSnapshot.child(Common.USER_EMAIL).getValue().toString();
                    otherEmail.setText(email);
                }else{
                    otherEmail.setText("Error");
                }
                if(dataSnapshot.child(Common.USER_EMAIL).exists()
                        && !dataSnapshot.child(Common.USER_EMAIL).getValue().toString().equals("")){
                    String email2 = dataSnapshot.child(Common.USER_EMAIL).getValue().toString();
                    otherEmail2.setText(email2);
                }else{
                    otherEmail2.setText("Error");
                }
                if(dataSnapshot.child(Common.EMERGENCY_CONTACT).child(Common.EMERGENCY_NAME).exists()
                        && !dataSnapshot.child(Common.EMERGENCY_CONTACT).child(Common.EMERGENCY_NAME).getValue().toString().equals("")){
                    String emerName = dataSnapshot.child(Common.EMERGENCY_CONTACT).child(Common.EMERGENCY_NAME).getValue().toString();
                    otherEmergencyName.setText(emerName);
                }else{
                    otherEmergencyName.setText("Emergency have not set");
                }
                if((dataSnapshot.child(Common.USER_PHONE).exists())
                        && !(dataSnapshot.child(Common.USER_PHONE).getValue().toString().equals(""))){
                    String phoneNumber = dataSnapshot.child(Common.USER_PHONE).getValue().toString();
                    otherPhone.setText(phoneNumber);
                }else{
                    otherPhone.setText("Phone number have not set");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void displayDialog() {
        AddFriendDialog addFriendDialog = new AddFriendDialog();
        addFriendDialog.show(getSupportFragmentManager(),"add friend");
    }


}
