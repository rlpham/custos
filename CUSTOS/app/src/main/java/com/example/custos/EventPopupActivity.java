package com.example.custos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.custos.utils.Common;
import com.example.custos.utils.Event;
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
import java.util.ArrayList;
import java.util.Calendar;

public class EventPopupActivity extends Activity {
    private Handler handler = new Handler();
    private Intent intent;
    private Button accept, decline,close;
    private TextView eventName, eventLocationName, eventArea, eventDate,
            eventTime,deletedEvent,myEventDetail,eventDetail,eventAcceptedTitle,eventDeletedTitle;
    private String otherEventId, otherUID, currentToken, otherToken,dateAccept, timeAccept;
    private String start_date, area, locationname, lat, lng, name, description, end_date, end_time, start_time;
    private ArrayList<User> invitedUsers;
    private FirebaseUser firebaseUser;
    private DatabaseReference userInfoRef, notificationRef, notificationRef3, eventRef, eventRefData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_popup_activity);
        eventDetail = findViewById(R.id.event_title_notification);
        eventDeletedTitle = findViewById(R.id.event_delete_title_notification);
        eventAcceptedTitle = findViewById(R.id.event_accepttitle_notification);
        deletedEvent = findViewById(R.id.deleted_event);
        myEventDetail = findViewById(R.id.event_mytitle_notification);
        close = findViewById(R.id.event_close);
        accept = findViewById(R.id.event_accept);
        decline = findViewById(R.id.event_decline);
        eventName = findViewById(R.id.event_name_notification);
        eventLocationName = findViewById(R.id.event_locationname_notification);
        eventArea = findViewById(R.id.event_area_notification);
        eventDate = findViewById(R.id.event_date_notification);
        eventTime = findViewById(R.id.event_time_notification);
        myEventDetail.setVisibility(View.INVISIBLE);
        deletedEvent.setVisibility(View.INVISIBLE);
        eventDeletedTitle.setVisibility(View.INVISIBLE);
        eventAcceptedTitle.setVisibility(View.INVISIBLE);
        eventDetail.setVisibility(View.VISIBLE);
        close.setVisibility(View.INVISIBLE);
        accept.setVisibility(View.VISIBLE);
        decline.setVisibility(View.VISIBLE);
        eventName.setVisibility(View.VISIBLE);
        eventArea.setVisibility(View.VISIBLE);
        eventLocationName.setVisibility(View.VISIBLE);
        eventTime.setVisibility(View.VISIBLE);
        eventDate.setVisibility(View.VISIBLE);

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

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        intent = getIntent();
        otherEventId = intent.getStringExtra("eventId");
        otherUID = intent.getStringExtra("userId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userInfoRef = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION).child(otherUID);
        notificationRef = FirebaseDatabase.getInstance().getReference(Common.NOTIFICATIONS)
                .child(firebaseUser.getUid())
                .child(Common.REQUEST_NOTIFICATION);
        notificationRef3 = FirebaseDatabase.getInstance().getReference(Common.NOTIFICATIONS)
                .child(otherUID)
                .child(Common.REQUEST_NOTIFICATION);
        eventRef = FirebaseDatabase.getInstance().getReference("user_event");
        eventRefData = FirebaseDatabase.getInstance().getReference("user_event").child(otherUID);


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)
                .child(firebaseUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentToken = dataSnapshot.child("userToken").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Calendar calendarAccept = Calendar.getInstance();
        SimpleDateFormat acceptDate = new SimpleDateFormat("dd-MMMM-yyyy");
        Calendar timeAcceptFriend = Calendar.getInstance();
        SimpleDateFormat acceptTime = new SimpleDateFormat("hh:mm a");
        dateAccept = acceptDate.format(calendarAccept.getTime());
        timeAccept = acceptTime.format(timeAcceptFriend.getTime());


        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationRef.child(otherEventId).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "removed notification", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "removed failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                eventRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).exists()) {
                            eventRef.child(firebaseUser.getUid()).child(otherEventId).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "delete declined event", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else if (!dataSnapshot.child(otherUID).child(otherEventId).exists()) {
                            Toast.makeText(getApplicationContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            eventRef.child(otherUID).child(otherEventId)
                                    .child("invited_users").child(firebaseUser.getUid())
                                    .child("status").setValue("declined");

                            notificationRef3.child(currentToken)
                                    .child("request_type").setValue("declined_invite")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                notificationRef3.child(currentToken)
                                                        .child("request_time").setValue(dateAccept + " at " + timeAccept);
                                                notificationRef3.child(currentToken)
                                                        .child("eventId").setValue(otherEventId);
                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                                                        .getReference(Common.USER_INFORMATION);

                                                databaseReference2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String nameCurr = dataSnapshot.child(firebaseUser.getUid())
                                                                .child(Common.USER_NAME).getValue().toString();
                                                        notificationRef3.child(currentToken)
                                                                .child(Common.FRIEND_NAME).setValue(nameCurr);
                                                        String uidCurr = dataSnapshot.child(firebaseUser.getUid())
                                                                .child(Common.UID).getValue().toString();
                                                        notificationRef3.child(currentToken)
                                                                .child(Common.UID).setValue(uidCurr);
                                                        String img = dataSnapshot.child(firebaseUser.getUid())
                                                                .child(Common.IMAGE_URL).getValue().toString();
                                                        notificationRef3.child(currentToken)
                                                                .child(Common.IMAGE_URL).setValue(img);
                                                        String token = dataSnapshot.child(firebaseUser.getUid())
                                                                .child("userToken").getValue().toString();
                                                        notificationRef3.child(currentToken)
                                                                .child("userToken").setValue(token);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }
                                        }
                                    });
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot eventSnapShot) {
                        if (!eventSnapShot.child(otherUID).child(otherEventId).exists()) {
                            Toast.makeText(getApplicationContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            eventRef.child(otherUID).child(otherEventId)
                                    .child("invited_users").child(firebaseUser.getUid())
                                    .child("status").setValue("accepted");

//                            notificationRef.child(otherEventId).removeValue()
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                Toast.makeText(getApplicationContext(), "removed notification", Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                Toast.makeText(getApplicationContext(), "removed failed", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
                            notificationRef3.child(currentToken)
                                    .child("request_type").setValue("accepted_invite")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                notificationRef3.child(currentToken)
                                                        .child("request_time").setValue(dateAccept + " at " + timeAccept);
                                                notificationRef3.child(currentToken)
                                                        .child("eventId").setValue(otherEventId);
                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                                                        .getReference(Common.USER_INFORMATION);

                                                databaseReference2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot userSnapShot) {
                                                        String nameCurr = userSnapShot.child(firebaseUser.getUid())
                                                                .child(Common.USER_NAME).getValue().toString();
                                                        notificationRef3.child(currentToken)
                                                                .child(Common.FRIEND_NAME).setValue(nameCurr);
                                                        String uidCurr = userSnapShot.child(firebaseUser.getUid())
                                                                .child(Common.UID).getValue().toString();
                                                        notificationRef3.child(currentToken)
                                                                .child(Common.UID).setValue(uidCurr);
                                                        String img = userSnapShot.child(firebaseUser.getUid())
                                                                .child(Common.IMAGE_URL).getValue().toString();
                                                        notificationRef3.child(currentToken)
                                                                .child(Common.IMAGE_URL).setValue(img);
                                                        String token = userSnapShot.child(firebaseUser.getUid())
                                                                .child("userToken").getValue().toString();
                                                        notificationRef3.child(currentToken)
                                                                .child("userToken").setValue(token);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }
                                        }
                                    });
                            if (otherUID != null) {
                                if (eventSnapShot.child(otherUID).child(otherEventId).exists()) {
                                    area = eventSnapShot.child(otherUID)
                                            .child(otherEventId).child("area").getValue().toString();
                                    start_date = eventSnapShot.child(otherUID)
                                            .child(otherEventId).child("start_date").getValue().toString();
                                    start_time = eventSnapShot.child(otherUID)
                                            .child(otherEventId).child("start_time").getValue().toString();
                                    end_date = eventSnapShot.child(otherUID)
                                            .child(otherEventId).child("end_date").getValue().toString();
                                    end_time = eventSnapShot.child(otherUID)
                                            .child(otherEventId).child("end_time").getValue().toString();
                                    description = eventSnapShot.child(otherUID)
                                            .child(otherEventId).child("description").getValue().toString();
                                    locationname = eventSnapShot.child(otherUID)
                                            .child(otherEventId).child("location_name").getValue().toString();
                                    name = eventSnapShot.child(otherUID)
                                            .child(otherEventId).child("name").getValue().toString();
                                    lat = eventSnapShot.child(otherUID)
                                            .child(otherEventId).child("location").child("latitude")
                                            .getValue().toString();
                                    lng = eventSnapShot.child(otherUID)
                                            .child(otherEventId).child("location").child("longitude")
                                            .getValue().toString();
                                    invitedUsers = new ArrayList<>();
                                    if (eventSnapShot.child(otherUID).child(otherEventId)
                                            .child("invited_users").exists()) {
                                        if (!eventSnapShot.child(otherUID).child(otherEventId)
                                                .child("invited_users")
                                                .getValue()
                                                .toString().equals("NONE")) {
                                            for (DataSnapshot snapshot : eventSnapShot.child(otherUID)
                                                    .child(otherEventId)
                                                    .child("invited_users").getChildren()) {
                                                User user = new User();
                                                user.setUserName(snapshot.child("name").getValue().toString());
                                                user.setUID(snapshot.getKey());
                                                invitedUsers.add(user);
                                            }
                                        }

                                    }
                                }

                            }

                            if (eventSnapShot.child(otherUID).child(otherEventId).exists()) {
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("name").setValue(name);
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("isSafetyEvent").setValue(false);
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("start_date").setValue(start_date);
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("start_time").setValue(start_time);
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("end_date").setValue(end_date);
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("end_time").setValue(end_time);
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("description").setValue(description);
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("location_name").setValue(locationname);
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("area").setValue(area);
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("isOwner").setValue("false");
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("location").child("latitude").setValue(Double.valueOf(lat));
                                eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                        .child("location").child("longitude").setValue(Double.valueOf(lng));
                                if (!invitedUsers.isEmpty()) {
                                    for (User user : invitedUsers) {
                                        eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                                .child("invited_users").child(user.getUID()).child("name")
                                                .setValue(user.getUserName());
                                    }
                                } else {
                                    eventRef.child(firebaseUser.getUid()).child(otherEventId)
                                            .child("invited_users").setValue("NONE");

                                }

                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DatabaseReference otherUserTokenRef = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)
                .child(otherUID);
        otherUserTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                otherToken = dataSnapshot.child("userToken").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).exists()) {
                    accept.setEnabled(false);
                    accept.setText("accepted");
                    eventAcceptedTitle.setVisibility(View.VISIBLE);
                    eventDetail.setVisibility(View.INVISIBLE);
                }
                if(dataSnapshot.child(otherUID).child(otherEventId).exists()
                        && dataSnapshot.child(otherUID).child(otherEventId).child("isOwner").getValue().toString().equals("false")){
                    decline.setEnabled(false);
                    decline.setVisibility(View.INVISIBLE);
                    accept.setVisibility(View.INVISIBLE);
                    close.setVisibility(View.VISIBLE);
                }
                if(!dataSnapshot.child(otherUID).child(otherEventId).exists()
                        && dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).exists()){
                    decline.setEnabled(false);
                    myEventDetail.setVisibility(View.VISIBLE);
                    decline.setVisibility(View.INVISIBLE);
                    accept.setVisibility(View.INVISIBLE);
                    eventDetail.setVisibility(View.INVISIBLE);
                    eventAcceptedTitle.setVisibility(View.INVISIBLE);
                    close.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(otherUID).child(otherEventId).child("name").exists()
                        && !dataSnapshot.child(otherUID).child(otherEventId).child("name").getValue().toString().equals("")) {
                    String eventNamedata = dataSnapshot.child(otherUID).child(otherEventId).child("name").getValue().toString();
                    eventName.setText(eventNamedata);
                }
                else if(!dataSnapshot.child(otherUID).child(otherEventId).child("name").exists()
                    && dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).child("name").exists()){
                    String curName = dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).child("name").getValue().toString();
                    eventName.setText(curName);
                }
                else{
                    accept.setVisibility(View.INVISIBLE);
                    decline.setVisibility(View.INVISIBLE);
                    eventName.setVisibility(View.INVISIBLE);
                    eventArea.setVisibility(View.INVISIBLE);
                    eventLocationName.setVisibility(View.INVISIBLE);
                    eventTime.setVisibility(View.INVISIBLE);
                    eventDate.setVisibility(View.INVISIBLE);
                    eventDetail.setVisibility(View.INVISIBLE);
                    eventDeletedTitle.setVisibility(View.VISIBLE);
                    deletedEvent.setVisibility(View.VISIBLE);
                    close.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child(otherUID).child(otherEventId).child("area").exists()
                        && !dataSnapshot.child(otherUID).child(otherEventId).child("area").getValue().toString().equals("")) {
                    String eventAreadata = dataSnapshot.child(otherUID).child(otherEventId).child("area").getValue().toString();
                    eventArea.setText(eventAreadata);
                }else if(!dataSnapshot.child(otherUID).child(otherEventId).child("area").exists()
                        && dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).child("area").exists()){
                    String curArea = dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).child("area").getValue().toString();
                    eventArea.setText(curArea);
                }
                if (dataSnapshot.child(otherUID).child(otherEventId).child("location_name").exists()
                        && !dataSnapshot.child(otherUID).child(otherEventId).child("location_name").getValue().toString().equals("")) {
                    String location_name = dataSnapshot.child(otherUID).child(otherEventId).child("location_name").getValue().toString();
                    eventLocationName.setText(location_name);
                }else if(!dataSnapshot.child(otherUID).child(otherEventId).child("location_name").exists()
                        && dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).child("location_name").exists()){
                    String curLocName = dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).child("location_name").getValue().toString();
                    eventLocationName.setText(curLocName);
                }
                if (dataSnapshot.child(otherUID).child(otherEventId).child("start_time").exists()
                        && !dataSnapshot.child(otherUID).child(otherEventId).child("start_time").getValue().toString().equals("")) {
                    String start_timeData = dataSnapshot.child(otherUID).child(otherEventId).child("start_time").getValue().toString();
                    eventTime.setText(start_timeData);
                }else if(!dataSnapshot.child(otherUID).child(otherEventId).child("start_time").exists()
                        && dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).child("start_time").exists()){
                    String curStartTime = dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).child("start_time").getValue().toString();
                    eventTime.setText(curStartTime);
                }
                if (dataSnapshot.child(otherUID).child(otherEventId).child("start_date").exists()
                        && !dataSnapshot.child(otherUID).child(otherEventId).child("start_date").getValue().toString().equals("")) {
                    String eventStartDate = dataSnapshot.child(otherUID).child(otherEventId).child("start_date").getValue().toString();
                    eventDate.setText(eventStartDate);
                }else if(!dataSnapshot.child(otherUID).child(otherEventId).child("start_date").exists()
                        && dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).child("start_date").exists()){
                    String curStartDate = dataSnapshot.child(firebaseUser.getUid()).child(otherEventId).child("start_date").getValue().toString();
                    eventDate.setText(curStartDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
