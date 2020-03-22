package com.example.custos;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class SetHomeLocationActivity extends AppCompatActivity {

    TextView address;
    Geocoder geocoder;
    Button saveButton;
    List<Address> addresses = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_location);
        address = findViewById(R.id.home_address);
        saveButton = findViewById(R.id.save_button);
        final String apiKey = "AIzaSyCjncU-Fe5pQKOc85zuGoR9XEs61joNajc";
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),apiKey);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetHomeLocationActivity.this,SecondSplashActivity.class);
                startActivity(intent);
            }
        });
        geocoder = new Geocoder(this,Locale.getDefault());


//        placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        //autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(new LatLng(40.263680,-76.890739), new LatLng(40.285519,-76.650589)));
// Set up a PlaceSelectionListener to handle the response.

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                SetHomeLocation setHomeLocation = new SetHomeLocation();
                Log.i("tester", "Place: " + latLng.latitude+"\n" + latLng.longitude);

                setHomeLocation.setLatitude(latLng.latitude);
                setHomeLocation.setLongtitude(latLng.longitude);
                FirebaseDatabase.getInstance().getReference("Home Location latlng")
                        .setValue(setHomeLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SetHomeLocationActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SetHomeLocationActivity.this,"Failed Save", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                System.out.println(setHomeLocation.getLatitude() + " " + setHomeLocation.getLongtitude());

                stringAddress(latLng);

            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("test" ,"An error occurred: " + status);
            }
        });

    }



    private void stringAddress(LatLng latLng){
        if(!addresses.isEmpty()){
            try {
                addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                if(addresses.size()>0){
                    String myAddress = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String fullAddress = myAddress + " " + city + " " + state;
                    address.setText(fullAddress);
                    System.out.println("---------------------------------"+myAddress + city);
                    Log.i("test","address: "+ myAddress + city + state);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
