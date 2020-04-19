package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.custos.utils.Common;
import com.example.custos.utils.Event;
import com.example.custos.utils.User;
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
    String location_name;
    ToolKit toolKit;

    //users that will be invited when creating event.
    ArrayList<User> selected;

    FirebaseUser firebaseUser;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        toolKit = new ToolKit();
        lv = findViewById(R.id.event_detail_invite_list);

        event_date_text_view = findViewById(R.id.event_detail_date);
        event_date_text_view.setInputType(InputType.TYPE_NULL);

        event_date_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialogx
                datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                event_date_text_view.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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
                            if(selectedHour == 12) {
                                selectedHour = 12;
                            } else {
                                selectedHour = selectedHour - 12;
                            }
                            am_pm = "PM";
                        } else {
                            if(selectedHour == 0) {
                                selectedHour = 12;
                            }
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
                getSupportFragmentManager().findFragmentById(R.id.event_location);


        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        //autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(new LatLng(40.263680,-76.890739), new LatLng(40.285519,-76.650589)));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place pl) {
                final LatLng latLng = pl.getLatLng();
                place = pl;
                lat = latLng.latitude;
                lon = latLng.longitude;
                location_name = pl.getName();
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
                    //    public Event(String ID, String name, String area, String date,
                    //      String time, String description, String location_name,
                    //      ArrayList<User> invited_users) {


                     final String id = createEventID();
                     Event event = new Event(id, name, getLocationText(lat, lon),
                            event_date_text_view.getText().toString(),
                            event_time_text_view.getText().toString(),
                            description, location_name);

                    DatabaseReference user_information = FirebaseDatabase.getInstance()
                            .getReference("user_event")
                            .child(firebaseUser.getUid())
                            .child(id);

                    user_information.child("name").setValue(event.getName());
                    user_information.child("date").setValue(event.getDate());
                    user_information.child("time").setValue(event.getTime());
                    user_information.child("description").setValue(event.getDescription());
                    user_information.child("area").setValue(event.getArea());
                    user_information.child("location").child("latitude").setValue(lat);
                    user_information.child("location").child("longitude").setValue(lon);
                    user_information.child("location_name").setValue(event.getLocation_name());

                    userReference = FirebaseDatabase.getInstance().getReference("Users");
                    userReference.child(firebaseUser.getUid()).child("contacts").addValueEventListener(new ValueEventListener() {

                        //TODO: REWRITE THIS TO INCORPORATE JUST ONE LIST VIEW AND THE SELECTED ARRAYLIST
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(int i = 0; i < lv.getCount(); i++) {
                                for(DataSnapshot element : dataSnapshot.getChildren()) {
                                    String invited_name = lv.getItemAtPosition(i).toString();
                                    String current_name = getNameFromValue(element.getValue().toString());
                                    if(invited_name.equals(current_name)) {
                                        //use element.getKey() to write into DB/user/<UID>/notifications/eventInvite
                                        DatabaseReference notification = userReference.child(element.getKey()).child("notifications").child("event_invites").child(id);
                                        notification.child("message").setValue(firebaseUser.getUid() + " has invited you to \"" + name + "\"");
                                        notification.child("timestamp").setValue(getCurrentTime());
                                        notification.child("sender").setValue(firebaseUser.getUid());

                                        //user_information.child(firebaseUser.getUid()).child(id).child("invited_users").child(element.getKey()).child("name").setValue(invited_name);

                                        //TODO: Append to root/<invited_user_id>/.... using 'selected' ArrayList
                                        
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
//                ArrayList<User> intent_selected = new ArrayList<User>();
//                for(int i = 0; i < lv.getCount(); i++) {
//                    for(int j = 0; j < selected.size(); j++) {
//                        if(lv.getItemAtPosition(i).toString().equals(selected.get(j).getUserName())) {
//                            intent_selected.add(selected.get(j));
//                        }
//                    }
//                }
                Intent intent = new Intent(v.getContext(), InviteGuestsActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", selected);
                intent.putExtra("BUNDLE", args);
                //intent.putStringArrayListExtra("selectedUsers", selected);
                startActivityForResult(intent, 18);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 18) {

            if(data != null) {
                //selected = data.getStringArrayListExtra("values");
                Bundle args = data.getBundleExtra("BUNDLE");
                selected = (ArrayList<User>) args.getSerializable("ARRAYLIST");
                String[] selectedNames = new String[selected.size()];
                for(int i = 0; i < selected.size(); i++) {
                    selectedNames[i] = selected.get(i).getUserName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.invite_guest_list_item, R.id.aaaaaaaa, selectedNames);
                lv = findViewById(R.id.event_detail_invite_list);
                lv.setAdapter(adapter);
            } else {

            }

        }
    }

    private String getNameFromValue(String d) {
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

    private String createEventID() {
        String id = "E" + generateNumber();
        return id;
    }

}
