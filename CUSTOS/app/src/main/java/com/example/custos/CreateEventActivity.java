package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.custos.utils.Common;
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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateEventActivity extends AppCompatActivity {
    TextView event_name_text_view;
    TextView event_description_text_view;
    TextView event_time_text_view;
    ListView lv;
    String name;
    String description;
    String date_time;
    private DatabaseReference user_information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        Places.initialize(getApplicationContext(),"AIzaSyCjncU-Fe5pQKOc85zuGoR9XEs61joNajc");
        //PlacesClient placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        //autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(new LatLng(40.263680,-76.890739), new LatLng(40.285519,-76.650589)));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
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
                event_name_text_view = findViewById(R.id.event_name_input);
                event_description_text_view = findViewById(R.id.event_description_input);
                event_time_text_view = findViewById(R.id.date_time_input);
                name = event_name_text_view.getText().toString();
                description = event_description_text_view.getText().toString();
                date_time = event_time_text_view.getText().toString();

                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                user_information = FirebaseDatabase.getInstance().getReference("user_event");
                user_information.orderByKey()
                        .equalTo(firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Common.event = new Event(firebaseUser.getUid(), name, description, date_time);
                                    DatabaseReference db = user_information.child(firebaseUser.getUid() + "/" + Common.event.getName());
                                    db.child("date_time").setValue(Common.event.getDate_time());
                                    db.child("description").setValue(Common.event.getDescription());
                                    db.child("location").child("latitude").setValue("33.7206");
                                    user_information.child(firebaseUser.getUid() + "/" + Common.event.getName() + "/location").child("longitude").setValue("-116.2156");
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
        });

        findViewById(R.id.invite_guests_button).setOnClickListener(new View.OnClickListener() {
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
            lv = findViewById(R.id.invited_list);
            lv.setAdapter(adapter);
        }


    }

}
