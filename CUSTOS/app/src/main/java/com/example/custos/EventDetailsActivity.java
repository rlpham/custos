package com.example.custos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.custos.utils.User;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {

    TextView event_detail_title;
    TextView event_detail_description;
    TextView event_detail_date;
    TextView event_detail_time;
    ListView event_detail_invite_list;
    Button edit_event_button;
    Button edit_event_guests_button;
    EditText event_detail_title_input;
    EditText event_detail_description_input;
    EditText event_detail_date_input;
    EditText event_detail_time_input;
    DatePickerDialog datePickerDialog;
    Place place;
    AutocompleteSupportFragment autocompleteFragment;
    boolean isEditMode = false;
    FirebaseUser firebaseUser;
    double lat;
    double lon;
    ToolKit toolKit;
    String location_name_input;
    ArrayList<User> invited_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        //Textviews that show event details
        event_detail_title = findViewById(R.id.event_detail_title);
        event_detail_description = findViewById(R.id.event_detail_description);
        event_detail_date = findViewById(R.id.event_detail_date);
        event_detail_time = findViewById(R.id.event_detail_time);
        event_detail_invite_list = findViewById(R.id.event_detail_invite_list);
        edit_event_button = findViewById(R.id.edit_event_button);
        edit_event_guests_button = findViewById(R.id.event_detail_invite_guests);

        //EditText that is enabled when "EDIT" is clicked
        event_detail_title_input = findViewById(R.id.event_detail_title_input);
        event_detail_description_input = findViewById(R.id.event_detail_description_input);
        event_detail_date_input = findViewById(R.id.event_detail_date_input);
        event_detail_time_input = findViewById(R.id.event_detail_time_input);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Intent intent = getIntent();

        final String id = intent.getStringExtra("event_id");
        String title = intent.getStringExtra("event_name");
        String description = intent.getStringExtra("event_desc");
        String date = intent.getStringExtra("event_date");
        String time = intent.getStringExtra("event_time");
        Bundle args = intent.getBundleExtra("BUNDLE");
        if(args != null) {
            invited_users = (ArrayList<User>) args.getSerializable("ARRAYLIST");
            String[] invited_users_adapter = new String[invited_users.size()];
            for(int i = 0; i < invited_users.size(); i++) {
                invited_users_adapter[i] = invited_users.get(i).getUserName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.invite_guest_list_item, R.id.aaaaaaaa, invited_users_adapter);
            event_detail_invite_list.setAdapter(adapter);
        }

        //String[] invited_users = intent.getStringExtra("invited_users").split(",");
        String location_name = intent.getStringExtra("location_name");

        toolKit = new ToolKit();

        event_detail_title.setText(title);
        event_detail_description.setText(description);
        event_detail_date.setText(date);
        event_detail_time.setText(time);

        //Initial filling in data
        event_detail_title_input.setText(event_detail_title.getText().toString());
        event_detail_description_input.setText(event_detail_description.getText().toString());
        event_detail_date_input.setText(event_detail_date.getText().toString());
        event_detail_time_input.setText(event_detail_time.getText().toString());



        Places.initialize(getApplicationContext(),"AIzaSyCjncU-Fe5pQKOc85zuGoR9XEs61joNajc");
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.event_detail_location);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place pl) {
                final LatLng latLng = pl.getLatLng();
                place = pl;
                lat = latLng.latitude;
                lon = latLng.longitude;
                location_name_input = pl.getName();
            }
            @Override
            public void onError(@NonNull Status status) {
            }
        });

        //autocompleteFragment.setText(location_name);


        edit_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditMode) {
                    isEditMode = true;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    edit_event_button.setText(R.string.invite_guests_done_button);

                    event_detail_title_input.setVisibility(View.VISIBLE);
                    event_detail_description_input.setVisibility(View.VISIBLE);
                    event_detail_date_input.setVisibility(View.VISIBLE);
                    event_detail_time_input.setVisibility(View.VISIBLE);

                    event_detail_title.setVisibility(View.INVISIBLE);
                    event_detail_description.setVisibility(View.INVISIBLE);
                    event_detail_date.setVisibility(View.INVISIBLE);
                    event_detail_time.setVisibility(View.INVISIBLE);

                    event_detail_title_input.setText(event_detail_title_input.getText().toString());
                    event_detail_description_input.setText(event_detail_description_input.getText().toString());
                    event_detail_date_input.setText(event_detail_date_input.getText().toString());
                    event_detail_time_input.setText(event_detail_time_input.getText().toString());

                    edit_event_guests_button.setVisibility(View.VISIBLE);


                    event_detail_title_input.requestFocus();
                } else {

                    if(!isInputValid(event_detail_title_input, event_detail_date_input, event_detail_time_input, place)) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid form", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        isEditMode = false;
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                        edit_event_button.setText(R.string.event_edit_button_label);


                        event_detail_title_input.setVisibility(View.INVISIBLE);
                        event_detail_description_input.setVisibility(View.INVISIBLE);
                        event_detail_date_input.setVisibility(View.INVISIBLE);
                        event_detail_time_input.setVisibility(View.INVISIBLE);

                        event_detail_title.setVisibility(View.VISIBLE);
                        event_detail_description.setVisibility(View.VISIBLE);
                        event_detail_date.setVisibility(View.VISIBLE);
                        event_detail_time.setVisibility(View.VISIBLE);

                        //TODO: Update event path with new credentials (name, description, date, time, location, invited_guests)
                        DatabaseReference db_root = FirebaseDatabase.getInstance().getReference("user_event").child(firebaseUser.getUid()).child(id);
                        DatabaseReference db_location = FirebaseDatabase.getInstance().getReference("user_event").child(firebaseUser.getUid()).child(id).child("location");

                        Map<String, Object> event_root_map = new HashMap<String, Object>();
                        Map<String, Object> event_location_map = new HashMap<String, Object>();
                        Map<String, Object> event_invited_users_map = new HashMap<String, Object>();
                        event_root_map.put("name", (event_detail_title_input.getText().toString()));
                        event_root_map.put("description", (event_detail_description_input.getText().toString()));
                        event_root_map.put("date", (event_detail_date_input.getText().toString()));
                        event_root_map.put("time", (event_detail_time_input.getText().toString()));
                        event_root_map.put("area", toolKit.getLocationText(lat, lon, EventDetailsActivity.this));
                        event_root_map.put("location_name", location_name_input);
                        event_location_map.put("latitude", lat);
                        event_location_map.put("longitude", lon);

//                        db.child("invited_users");

                        db_root.updateChildren(event_root_map);
                        db_location.updateChildren(event_location_map);

                        event_detail_title.setText(event_detail_title_input.getText().toString());
                        event_detail_description.setText(event_detail_description_input.getText().toString());
                        event_detail_date.setText(event_detail_date_input.getText().toString());
                        event_detail_time.setText(event_detail_time_input.getText().toString());

                        edit_event_guests_button.setVisibility(View.INVISIBLE);



                    }

                }

            }
        });

        event_detail_date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(EventDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                event_detail_date_input.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
                                event_detail_date.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        event_detail_time_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EventDetailsActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
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
                            event_detail_time_input.setText(selectedHour + ":0" + selectedMinute + " " + am_pm);
                            event_detail_time.setText(selectedHour + ":0" + selectedMinute + " " + am_pm);
                        } else {
                            event_detail_time_input.setText(selectedHour + ":" + selectedMinute + " " + am_pm);
                            event_detail_time.setText(selectedHour + ":" + selectedMinute + " " + am_pm);
                        }

                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        Places.initialize(getApplicationContext(),"AIzaSyCjncU-Fe5pQKOc85zuGoR9XEs61joNajc");
        //PlacesClient placesClient = Places.createClient(this);

        edit_event_guests_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                ArrayList<String> selected = new ArrayList<String>();
                for(int i = 0; i < event_detail_invite_list.getCount(); i++) {
                    selected.add(event_detail_invite_list.getItemAtPosition(i).toString());
                }
                Intent intent = new Intent(v.getContext(), InviteGuestsActivity.class);
                intent.putStringArrayListExtra("selected", selected);
                startActivityForResult(intent, 18);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 18) {

            if(data != null) {
                Bundle args = data.getBundleExtra("BUNDLE");
                ArrayList<User> selected = (ArrayList<User>) args.getSerializable("ARRAYLIST");
                String[] selected_adapter = new String[selected.size()];
                for(int i = 0; i < selected.size(); i++) {
                    selected_adapter[i] = selected.get(i).getUserName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.invite_guest_list_item, R.id.aaaaaaaa, selected_adapter);
                event_detail_invite_list = findViewById(R.id.event_detail_invite_list);
                event_detail_invite_list.setAdapter(adapter);
            } else {

            }

        }
    }

    private boolean isInputValid(TextView name, TextView date, TextView time, Place place) {
        if((!name.getText().toString().equals("")) &&  (!date.getText().toString().equals("")) && (!time.getText().toString().equals("")) && (place != null)) {
            return true;
        } else {
            return false;
        }
    }

    private String getLocationText(double latitude, double longitude) {
        String locationText = "";
        Geocoder geocoder;
        geocoder = new Geocoder(EventDetailsActivity.this, Locale.getDefault());
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
}
