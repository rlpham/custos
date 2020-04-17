package com.example.custos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.custos.utils.AddFriendDialog;
import com.example.custos.utils.Common;
import com.example.custos.utils.LoadingDialog;
import com.example.custos.utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserActivity extends AppCompatActivity {
    public static final String TAG = "OtherUserActivity";
    private String CURRENT_STATE;
    private DatabaseReference notificationsRef;
    CircleImageView otherUserImage;
    TextView otherName,
            otherName2,
            otherEmail,
            otherEmail2,
            otherEmergencyName,
            otherPhone,
            backButton;
    Button addFriend, declineRequest;
    String currentUID, otherUserId, saveCurrentDate;
    String timeSent,dateSent,dateAccept,timeAccept;
    FirebaseUser firebaseUser;
    DatabaseReference friendRequestReference;
    DatabaseReference databaseReference;
    DatabaseReference acceptFriends;
    Intent intent;
    Handler handler = new Handler();


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
        declineRequest = findViewById(R.id.declineRequest);
        CURRENT_STATE = "not_friends";
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        final int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    decorView.setSystemUiVisibility(uiOptions);
                                }
                            }, 2000);
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });
        intent = getIntent();
        currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        otherUserId = intent.getStringExtra("userid");

        declineRequest.setVisibility(View.INVISIBLE);
        declineRequest.setEnabled(false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION).child(otherUserId);
        friendRequestReference = FirebaseDatabase.getInstance().getReference(Common.FRIEND_REQUEST);

        notificationsRef = FirebaseDatabase.getInstance().getReference(Common.NOTIFICATIONS);

        acceptFriends = FirebaseDatabase.getInstance().getReference("Friends");
        if (!currentUID.equals(otherUserId)) {
            addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OtherUserActivity.this, R.style.Chill);
                    addFriend.setEnabled(false);
                    if (CURRENT_STATE.equals("not_friends")) {
                        alertDialog.setTitle("Request Friend");
                        alertDialog.setMessage("Add this user as friend?");
                        alertDialog.setIcon(R.drawable.ic_person_add_black_24dp);
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendFriendRequest();
                                final LoadingDialog loadingDialog = new LoadingDialog(OtherUserActivity.this);
                                loadingDialog.startLoadingDialog();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismissDialog();
                                    }
                                }, 1500);
                            }
                        });
                        AlertDialog alertDialog2 = alertDialog.create();

                        // Set alertDialog "not focusable" so nav bar still hiding:
                        alertDialog2.getWindow().
                                setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                        // Set full-sreen mode (immersive sticky):
                        alertDialog2.getWindow().getDecorView().setSystemUiVisibility(Common.ui_flags);

                        // Show the alertDialog:
                        alertDialog2.show();

                        // Set dialog focusable so we can avoid touching outside:
                        alertDialog2.getWindow().
                                clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                    }
                    if (CURRENT_STATE.equals("request_sent")) {
                        alertDialog.setTitle("Cancel Request");
                        alertDialog.setMessage("Cancel adding friend?");
                        alertDialog.setIcon(R.drawable.ic_person_add_black_24dp);
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cancelFriendRequest();
                                final LoadingDialog loadingDialog = new LoadingDialog(OtherUserActivity.this);
                                loadingDialog.startLoadingDialog();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismissDialog();
                                    }
                                }, 1500);
                            }
                        });
                        AlertDialog alertDialog2 = alertDialog.create();

                        // Set alertDialog "not focusable" so nav bar still hiding:
                        alertDialog2.getWindow().
                                setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                        // Set full-sreen mode (immersive sticky):
                        alertDialog2.getWindow().getDecorView().setSystemUiVisibility(Common.ui_flags);

                        // Show the alertDialog:
                        alertDialog2.show();

                        // Set dialog focusable so we can avoid touching outside:
                        alertDialog2.getWindow().
                                clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                    }
                    if (CURRENT_STATE.equals("request_received")) {
                        acceptRequest();
                        final LoadingDialog loadingDialog = new LoadingDialog(OtherUserActivity.this);
                        loadingDialog.startLoadingDialog();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                            }
                        }, 1500);
                    }
                    if (CURRENT_STATE.equals("friends")) {
                        alertDialog.setTitle("Unfriend");
                        alertDialog.setMessage("Are you sure you want to unfriend this person?");
                        alertDialog.setIcon(R.drawable.ic_person_add_black_24dp);
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                addFriend.setEnabled(true);
                            }
                        });
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                unfriend();
                                final LoadingDialog loadingDialog = new LoadingDialog(OtherUserActivity.this);
                                loadingDialog.startLoadingDialog();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismissDialog();
                                    }
                                }, 1000);
                            }
                        });
                        AlertDialog alertDialog2 = alertDialog.create();

                        // Set alertDialog "not focusable" so nav bar still hiding:
                        alertDialog2.getWindow().
                                setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                        // Set full-sreen mode (immersive sticky):
                        alertDialog2.getWindow().getDecorView().setSystemUiVisibility(Common.ui_flags);

                        // Show the alertDialog:
                        alertDialog2.show();

                        // Set dialog focusable so we can avoid touching outside:
                        alertDialog2.getWindow().
                                clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                    }

                }
            });
        } else {
            addFriend.setVisibility(View.INVISIBLE);
        }
        maintainButtonText();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getImageURL().equals("default")) {
                    otherUserImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(otherUserImage);
                }
                if (dataSnapshot.child(Common.USER_NAME).exists()
                        && !dataSnapshot.child(Common.USER_NAME).getValue().toString().equals("")) {
                    String name = dataSnapshot.child(Common.USER_NAME).getValue().toString();
                    otherName.setText(name);
                } else {
                    otherName.setText("User didnt set name");
                }
                if (dataSnapshot.child(Common.USER_NAME).exists()
                        && !dataSnapshot.child(Common.USER_NAME).getValue().toString().equals("")) {
                    String name2 = dataSnapshot.child(Common.USER_NAME).getValue().toString();
                    otherName2.setText(name2);
                } else {
                    otherName2.setText("User didnt set name");
                }
                if (dataSnapshot.child(Common.USER_EMAIL).exists()
                        && !dataSnapshot.child(Common.USER_EMAIL).getValue().toString().equals("")) {
                    String email = dataSnapshot.child(Common.USER_EMAIL).getValue().toString();
                    otherEmail.setText(email);
                } else {
                    otherEmail.setText("Error");
                }
                if (dataSnapshot.child(Common.USER_EMAIL).exists()
                        && !dataSnapshot.child(Common.USER_EMAIL).getValue().toString().equals("")) {
                    String email2 = dataSnapshot.child(Common.USER_EMAIL).getValue().toString();
                    otherEmail2.setText(email2);
                } else {
                    otherEmail2.setText("Error");
                }
                if (dataSnapshot.child(Common.EMERGENCY_CONTACT).child(Common.EMERGENCY_NAME).exists()
                        && !dataSnapshot.child(Common.EMERGENCY_CONTACT).child(Common.EMERGENCY_NAME).getValue().toString().equals("")) {
                    String emerName = dataSnapshot.child(Common.EMERGENCY_CONTACT).child(Common.EMERGENCY_NAME).getValue().toString();
                    otherEmergencyName.setText(emerName);
                } else {
                    otherEmergencyName.setText("Emergency have not set");
                }
                if ((dataSnapshot.child(Common.USER_PHONE).exists())
                        && !(dataSnapshot.child(Common.USER_PHONE).getValue().toString().equals(""))) {
                    String phoneNumber = dataSnapshot.child(Common.USER_PHONE).getValue().toString();
                    otherPhone.setText(phoneNumber);
                } else {
                    otherPhone.setText("Phone number have not set");
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void unfriend() {
        acceptFriends.child(currentUID).child(otherUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            acceptFriends.child(otherUserId).child(currentUID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                addFriend.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
                                                addFriend.setText("Add Friend");
                                                declineRequest.setVisibility(View.INVISIBLE);
                                                declineRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void acceptRequest() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child(otherUserId)
                        .child(Common.USER_EMAIL).getValue().toString();
                acceptFriends.child(currentUID).child(otherUserId).child(Common.FRIEND_EMAIL).setValue(email);
                String img = dataSnapshot.child(otherUserId)
                        .child(Common.IMAGE_URL).getValue().toString();
                acceptFriends.child(currentUID).child(otherUserId).child(Common.IMAGE_URL).setValue(img);
                String name = dataSnapshot.child(otherUserId)
                        .child(Common.USER_NAME).getValue().toString();
                acceptFriends.child(currentUID).child(otherUserId).child(Common.FRIEND_NAME).setValue(name);
                String uid = dataSnapshot.child(otherUserId)
                        .child(Common.UID).getValue().toString();
                acceptFriends.child(currentUID).child(otherUserId).child(Common.UID).setValue(uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child(currentUID)
                        .child(Common.USER_EMAIL).getValue().toString();
                acceptFriends.child(otherUserId).child(currentUID).child(Common.FRIEND_EMAIL).setValue(email);
                String img = dataSnapshot.child(currentUID)
                        .child(Common.IMAGE_URL).getValue().toString();
                acceptFriends.child(otherUserId).child(currentUID).child(Common.IMAGE_URL).setValue(img);
                String name = dataSnapshot.child(currentUID)
                        .child(Common.USER_NAME).getValue().toString();
                acceptFriends.child(otherUserId).child(currentUID).child(Common.FRIEND_NAME).setValue(name);
                String uid = dataSnapshot.child(currentUID)
                        .child(Common.UID).getValue().toString();
                acceptFriends.child(otherUserId).child(currentUID).child(Common.UID).setValue(uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        acceptFriends.child(currentUID).child(otherUserId).child(Common.FRIEND_DATE).setValue(saveCurrentDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            acceptFriends.child(otherUserId).child(currentUID).child(Common.FRIEND_DATE).setValue(saveCurrentDate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                friendRequestReference.child(currentUID).child(otherUserId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    friendRequestReference.child(otherUserId).child(currentUID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        addFriend.setEnabled(true);
                                                                                        CURRENT_STATE = "friends";
                                                                                        addFriend.setText("Unfriend");

                                                                                        declineRequest.setVisibility(View.INVISIBLE);
                                                                                        declineRequest.setEnabled(false);
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });


        Calendar calendarAccept = Calendar.getInstance();
        SimpleDateFormat acceptDate = new SimpleDateFormat("dd-MMMM-yyyy");
        Calendar timeAcceptFriend = Calendar.getInstance();
        SimpleDateFormat acceptTime = new SimpleDateFormat("hh:mm a");
        dateAccept = acceptDate.format(calendarAccept.getTime());
        timeAccept = acceptTime.format(timeAcceptFriend.getTime());

        notificationsRef.child(currentUID).child(otherUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"removed notification",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"removed failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        notificationsRef.child(otherUserId).child(currentUID)
                .child("request_type").setValue("acceptedFriendRequest")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            notificationsRef.child(otherUserId).child(currentUID).child("request_time").setValue(dateAccept + " at "+ timeAccept);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);

                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String nameCurr = dataSnapshot.child(currentUID)
                                            .child(Common.USER_NAME).getValue().toString();
                                    notificationsRef.child(otherUserId).child(currentUID).child(Common.FRIEND_NAME).setValue(nameCurr);
                                    String uidCurr = dataSnapshot.child(currentUID)
                                            .child(Common.UID).getValue().toString();
                                    notificationsRef.child(otherUserId).child(currentUID).child(Common.UID).setValue(uidCurr);
                                    String img = dataSnapshot.child(currentUID)
                                            .child(Common.IMAGE_URL).getValue().toString();
                                    notificationsRef.child(otherUserId).child(currentUID).child(Common.IMAGE_URL).setValue(img);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });




    }

    public void cancelFriendRequest() {
        friendRequestReference.child(currentUID).child(otherUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            friendRequestReference.child(otherUserId).child(currentUID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                addFriend.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
                                                addFriend.setText("Add Friend");
                                                declineRequest.setVisibility(View.INVISIBLE);
                                                declineRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });

        notificationsRef.child(otherUserId).child(currentUID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"removed notification",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"removed failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void maintainButtonText() {
        friendRequestReference.child(currentUID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(otherUserId).child("request_type").exists()) {
                            String request_type = dataSnapshot.child(otherUserId).child("request_type").getValue().toString();
                            if (request_type.equals("sent")) {
                                CURRENT_STATE = "request_sent";
                                addFriend.setText("Cancel Friend Request");
                                declineRequest.setVisibility(View.INVISIBLE);
                                declineRequest.setEnabled(false);
                            }
                            if (request_type.equals("received")) {
                                CURRENT_STATE = "request_received";
                                addFriend.setText("Accept");

                                declineRequest.setVisibility(View.VISIBLE);
                                declineRequest.setEnabled(true);

                                declineRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cancelFriendRequest();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        acceptFriends.child(currentUID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(otherUserId).exists()) {
                            CURRENT_STATE = "friends";
                            addFriend.setText("Unfriend");
                            declineRequest.setVisibility(View.INVISIBLE);
                            declineRequest.setEnabled(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendFriendRequest() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        Calendar time = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        dateSent = currentDate.format(calendar.getTime());
        timeSent = currentTime.format(time.getTime());

        notificationsRef.child(otherUserId).child(currentUID)
                .child("request_type").setValue("receivedFriendRequest")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            notificationsRef.child(otherUserId).child(currentUID).child("request_time").setValue(dateSent + " at "+ timeSent);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);

                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String nameCurr = dataSnapshot.child(currentUID)
                                            .child(Common.USER_NAME).getValue().toString();
                                    notificationsRef.child(otherUserId).child(currentUID).child(Common.FRIEND_NAME).setValue(nameCurr);
                                    String uidCurr = dataSnapshot.child(currentUID)
                                            .child(Common.UID).getValue().toString();
                                    notificationsRef.child(otherUserId).child(currentUID).child(Common.UID).setValue(uidCurr);
                                    String img = dataSnapshot.child(currentUID)
                                            .child(Common.IMAGE_URL).getValue().toString();
                                    notificationsRef.child(otherUserId).child(currentUID).child(Common.IMAGE_URL).setValue(img);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
        friendRequestReference.child(currentUID).child(otherUserId).child("request_time").setValue(dateSent);
        friendRequestReference.child(otherUserId).child(currentUID).child("request_time").setValue(dateSent);

        friendRequestReference.child(currentUID).child(otherUserId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            friendRequestReference.child(otherUserId).child(currentUID)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                addFriend.setEnabled(true);
                                                CURRENT_STATE = "request_sent";
                                                addFriend.setText("Cancel Friend Request");

                                                declineRequest.setVisibility(View.INVISIBLE);
                                                declineRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void displayDialog() {
        AddFriendDialog addFriendDialog = new AddFriendDialog();
        addFriendDialog.show(getSupportFragmentManager(), "add friend");
    }


}
