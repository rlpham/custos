package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.custos.utils.Common;
import com.example.custos.utils.Event;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CreateEventActivity extends AppCompatActivity {
    TextView event_name_text_view;
    TextView event_description_text_view;
    TextView event_date_text_view;
    TextView event_time_text_view;
    ListView lv;
    String name;
    String description;
    ArrayList<String> uids = new ArrayList<String>();
    double lat;
    double lon;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Place place;


    FirebaseUser firebaseUser;
    private DatabaseReference user_information;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        event_date_text_view = findViewById(R.id.event_detail_date);
        event_date_text_view.setInputType(InputType.TYPE_NULL);

        event_date_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                event_date_text_view.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        event_time_text_view = findViewById(R.id.event_detail_time);
        event_time_text_view.setInputType(InputType.TYPE_NULL);

        event_time_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String am_pm = "";
                        if(selectedHour > 11) {
                            selectedHour = selectedHour - 12;
                            am_pm = "PM";
                        } else {
                            am_pm = "AM";
                        }

                        if(selectedMinute < 10) {
                            event_time_text_view.setText(selectedHour + ":0" + selectedMinute + " " + am_pm);
                        } else {
                            event_time_text_view.setText(selectedHour + ":" + selectedMinute + " " + am_pm);
                        }

                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        Places.initialize(getApplicationContext(),"AIzaSyCjncU-Fe5pQKOc85zuGoR9XEs61joNajc");
        //PlacesClient placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.event_detail_location);


        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        //autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(new LatLng(40.263680,-76.890739), new LatLng(40.285519,-76.650589)));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place pl) {
                final LatLng latLng = pl.getLatLng();
                place = pl;
                lat = latLng.latitude;
                lon = latLng.longitude;
            }

            @Override
            public void onError(@NonNull Status status) {
            }
        });

        //CREATE EVENT POST REQUEST
        findViewById(R.id.create_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //POST DATA HERE
                event_name_text_view = findViewById(R.id.event_detail_name);
                event_description_text_view = findViewById(R.id.event_detail_description);
                event_date_text_view = findViewById(R.id.event_date);
                event_time_text_view = findViewById(R.id.event_detail_time);
                final ListView invited_list = findViewById(R.id.event_detail_invite_list);

                event_date_text_view = findViewById(R.id.event_detail_date);
                event_time_text_view = findViewById(R.id.event_detail_time);

                if(!isInputValid(event_name_text_view, event_date_text_view, event_time_text_view, place)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid form", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    name = event_name_text_view.getText().toString();
                    description = event_description_text_view.getText().toString();

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    //create event to database -> DB/user_event/<uid>/<event_name>
                    user_information = FirebaseDatabase.getInstance().getReference("user_event");
                    Common.event = new Event(firebaseUser.getUid(), name, description, event_time_text_view.getText().toString());
                    user_information.child(firebaseUser.getUid() + "/" + Common.event.getName());
                    user_information.child(firebaseUser.getUid() + "/" + Common.event.getName()).child("date").setValue(event_date_text_view.getText().toString());
                    user_information.child(firebaseUser.getUid() + "/" + Common.event.getName()).child("time").setValue(event_time_text_view.getText().toString());
                    user_information.child(firebaseUser.getUid() + "/" + Common.event.getName()).child("description").setValue(Common.event.getDescription());
                    user_information.child(firebaseUser.getUid() + "/" + Common.event.getName()).child("area").setValue(getLocationText(lat, lon));
                    user_information.child(firebaseUser.getUid() + "/" + Common.event.getName()).child("location").child("latitude").setValue(lat);
                    user_information.child(firebaseUser.getUid() + "/" + Common.event.getName() + "/location").child("longitude").setValue(lon);

                    //Gather list uids of invited guests
                    userReference = FirebaseDatabase.getInstance().getReference("Users");
                    userReference.child(firebaseUser.getUid()).child("contacts").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String id = "E" + generateNumber();
                            String strDate = getCurrentTime();
                            for(int i = 0; i < invited_list.getCount(); i++) {
                                for(DataSnapshot element : dataSnapshot.getChildren()) {
                                    String invited_name = invited_list.getItemAtPosition(i).toString();
                                    String current_name = getNameFromValue(element.getValue().toString());
                                    if(invited_name.equals(current_name)) {
                                        //use element.getKey() to write into DB/user/<UID>/notifications/eventInvite
                                        DatabaseReference notification = userReference.child(element.getKey()).child("notifications").child("event_invites").child(id);
                                        notification.child("message").setValue("<SENDER UID> has invited you to an event <EVENT NAME>");
                                        notification.child("timestamp").setValue(strDate);
                                        notification.child("sender").setValue(firebaseUser.getUid());
                                        user_information.child(firebaseUser.getUid() + "/" + Common.event.getName()).child("invited_users").child(element.getKey()).child("name").setValue(invited_name);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Intent intent = new Intent(v.getContext(), MainEventListActivity.class);
                    onActivityResult(1,1,intent);
                    setResult(1, intent);
                    finish();

                }



            }
        });

        findViewById(R.id.event_detail_edit_guests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InviteGuestsActivity.class);
                startActivityForResult(intent, 18);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 18) {
            ArrayList<String> selected = data.getStringArrayListExtra("values");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selected);
            lv = findViewById(R.id.event_detail_invite_list);
            lv.setAdapter(adapter);
        }
    }

    private String getNameFromValue(String d) {
        //"{number=12324234, name=Madison Beer}"
        int initialIndex = d.indexOf("name=") + 5;
        int lastIndex = d.indexOf("}");
        String name = d.substring(initialIndex, lastIndex);
        return name;
    }

    private String generateNumber() {
        String number = "";
        for(int i = 0; i < 4; i++) {
            number += String.valueOf(new Random().nextInt(10));
        }
        return number;
    }

    private String getLocationText(double latitude, double longitude) {
        String locationText = "";
        Geocoder geocoder;
        geocoder = new Geocoder(CreateEventActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude,1);
            locationText = addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
            Log.d("mylog","complete address: " + addresses.toString());
            Log.d("mylog","address: " + locationText);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationText;
    }

    private boolean isInputValid(TextView name, TextView date, TextView time, Place place) {

        if((!name.getText().toString().equals("")) &&  (!date.getText().toString().equals("")) && (!time.getText().toString().equals("")) && (place != null)) {
            return true;
        } else {
            return false;
        }
    }

    private String getCurrentTime() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

}
