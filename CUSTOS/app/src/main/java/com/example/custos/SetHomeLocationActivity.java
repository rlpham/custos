package com.example.custos;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custos.utils.Common;
import com.example.custos.utils.User;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class SetHomeLocationActivity extends AppCompatActivity {

    TextView address;
    //Geocoder geocoder;
    Button saveButton;
    Button backButton;
    ProgressBar progressBar;
    List<Address> addresses = new ArrayList<>();
    User user = new User();
    SetHomeLocation setHomeLocation = new SetHomeLocation();
    DatabaseReference databaseReference;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_location);
        address = findViewById(R.id.home_address);
        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progress_circular3);
        progressBar.setVisibility(View.INVISIBLE);
        final String apiKey = "AIzaSyCjncU-Fe5pQKOc85zuGoR9XEs61joNajc";
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),apiKey);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng myCoordinates = new LatLng(setHomeLocation.getLatitude(),setHomeLocation.getLongtitude());
                user.setUserAddress(getFullAddress(myCoordinates));
                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)
                        .child(user.getUID())
                        .child(Common.USER_ADDRESS)
                        .child(Common.HOME_LOC)
                        .setValue(user.getUserAddress())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SetHomeLocationActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(SetHomeLocationActivity.this,"Failed Save", Toast.LENGTH_SHORT).show();
                                }
                            }
                });


//                databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(!(dataSnapshot.child(firebaseUser.getUid())
//                                .child(Common.USER_ADDRESS)
//                                .child(Common.HOME_LOC)
//                                .getValue()
//                                .toString()
//                                .equals(" "))){
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });



                progressBar.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SetHomeLocationActivity.this,SecondSplashActivity.class);
                        startActivity(intent);
                    }
                },2500);

            }
        });
        //geocoder = new Geocoder(this,Locale.getDefault());
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                user.setUID(firebaseUser.getUid());
                if((dataSnapshot.child(firebaseUser.getUid())
                        .child(Common.USER_ADDRESS)
                        .child(Common.HOME_LOC)
                        .exists())
                        && !(dataSnapshot.child(firebaseUser.getUid())
                        .child(Common.USER_ADDRESS)
                        .child(Common.HOME_LOC)
                        .getValue()
                        .toString().equals(" "))){
                    String fullAddress = dataSnapshot.child(user.getUID())
                            .child(Common.USER_ADDRESS)
                            .child(Common.HOME_LOC)
                            .getValue().toString();
                    address.setText(fullAddress);
                }else if((dataSnapshot.child(firebaseUser.getUid())
                        .child(Common.USER_ADDRESS)
                        .child(Common.HOME_LOC)
                        .exists()) && (dataSnapshot.child(firebaseUser.getUid())
                        .child(Common.USER_ADDRESS)
                        .child(Common.HOME_LOC)
                        .getValue()
                        .toString().equals(" "))){
                    address.setText("Something went wrong try again later!");
                }
                else {
                    address.setText("User have not set their home location");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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


                Log.i("tester", "Place: " + latLng.latitude+"\n" + latLng.longitude);

                setHomeLocation.setLatitude(latLng.latitude);
                setHomeLocation.setLongtitude(latLng.longitude);
                //final String fullAd = stringAddress(latLng.latitude,latLng.longitude);


                FirebaseDatabase.getInstance().getReference("Home Location latlng")
                        .setValue(setHomeLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                           // Toast.makeText(SetHomeLocationActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SetHomeLocationActivity.this,"Failed Save", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)
                        .child(user.getUID())
                        .child(Common.USER_ADDRESS)
                        .child(Common.USER_HOME_LAT)
                        .setValue(setHomeLocation.getLatitude())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SetHomeLocationActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(SetHomeLocationActivity.this,"Failed Save", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)
                        .child(user.getUID())
                        .child(Common.USER_ADDRESS)
                        .child(Common.USER_HOME_LNG)
                        .setValue(setHomeLocation.getLongtitude())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
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


            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("test" ,"An error occurred: " + status);
            }
        });

    }
    private String getFullAddress(LatLng myCoordinates) {
        String fullAddress = " ";
        Geocoder geocoder;
        geocoder = new Geocoder(SetHomeLocationActivity.this,Locale.getDefault());
        try {
            List<Address> addresses2 = geocoder.getFromLocation(myCoordinates.latitude,myCoordinates.longitude,1);
            fullAddress = addresses2.get(0).getAddressLine(0);
            Log.d("mylog","complete address: " + addresses2.toString());
            Log.d("mylog","address: " + fullAddress);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fullAddress;
    }


    private String stringAddress(double latitude, double longtitude){
        String fullAddress="";
        if(!addresses.isEmpty()){
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude,longtitude,1);
                if(addresses.size()>0){
                    String myAddress = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    fullAddress = myAddress + " " + city + " " + state;
                    return fullAddress;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }return fullAddress;

    }
}
