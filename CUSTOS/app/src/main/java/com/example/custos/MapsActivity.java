package com.example.custos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.custos.utils.Common;
import com.example.custos.utils.FirstTimeLoginDialog;
import com.example.custos.utils.User;
import com.example.custos.utils.UserLocation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.TypeFilter;
//import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private SearchView searchView;
    private FragmentTransaction transaction;
    private FusedLocationProviderClient fusedLocationClient;
    private final int ok = 0;

    GoogleSignInClient googleSignInClient;
    //Testcode below

    int dangerZoneRequestCode = 0;


    public void openFragment(Fragment fragment) {
/*
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
*/

        transaction = getSupportFragmentManager().beginTransaction();

        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();


    }

    public void closeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            getSupportFragmentManager().popBackStackImmediate();
        }

    }

    private DatabaseReference db, db2, db3, db4;
    //tillhere




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
//rahul test

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
        //till here rahul out

        // Dale's Code
        //mMap.setOnMarkerClickListener(this);
        //
        userList = new ArrayList<>();
        //   db = FirebaseDatabase.getInstance().getReference("Users").child("rlpham18").child("event").child("e1234");
        // db2 = FirebaseDatabase.getInstance().getReference("Users").child("rlpham18").child("event").child("e1213");
        db3 = FirebaseDatabase.getInstance().getReference("User Information");
        db4 = FirebaseDatabase.getInstance().getReference("userLocation");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Hoang testing for search bar
        searchView = findViewById(R.id.search_view_mapActivity);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);
        //end
        // bottomNavigation = findViewById(R.id.bottom_navigation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Rahul TestCode below


        final TextView dangerzonebutton = findViewById(R.id.mapsDsngerZoneButton);
        dangerzonebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, DangerZoneActivity.class);
                int requestCode = intent.getIntExtra("dangervalue", 3);
                //startActivityForResult(intent,requestCode);
                startActivityForResult(intent, requestCode);
                //startActivity(intent);
            }
        });
        final Button friendmapbutton = findViewById(R.id.mapsfriendbutton);
        final RelativeLayout mapsfriendlayoutbackgorund = findViewById(R.id.mapsBackground);
        final RelativeLayout friendsbackground = findViewById(R.id.mapsfriendzone);
        final RelativeLayout evntsbackground = findViewById(R.id.mapseventzone);
        final Spinner spinner = (Spinner) findViewById(R.id.mapsEventSelection);
        //mapsfriendlayoutbackgorund.setVisibility(View.VISIBLE);
        mapsfriendlayoutbackgorund.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                friendsbackground.setVisibility(View.GONE);
                mapsfriendlayoutbackgorund.setVisibility(View.GONE);
                evntsbackground.setVisibility(View.GONE);
                friendmapbutton.setVisibility(View.GONE);
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_maps);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_events:
                        dangerzonebutton.setVisibility(View.GONE);
                        searchView.setVisibility(View.GONE);
                        spinner.setVisibility(View.GONE);
                        //  friendmapbutton.setVisibility(View.GONE);
                        //      mapsback.setVisibility(View.GONE);
                        friendsbackground.setVisibility(View.GONE);

                        openFragment(MainEventListActivity.newInstance());
                        return true;
                    case R.id.navigation_notifications:
                        dangerzonebutton.setVisibility(View.GONE);
                        searchView.setVisibility(View.GONE);
                        spinner.setVisibility(View.GONE);
                        friendsbackground.setVisibility(View.GONE);
                        //    mapsback.setVisibility(View.GONE);
                        openFragment(NotificationActivity.newInstance());
                        return true;
                    case R.id.navigation_friends:
                        dangerzonebutton.setVisibility(View.GONE);
                        spinner.setVisibility(View.GONE);
                        searchView.setVisibility(View.GONE);
                        friendsbackground.setVisibility(View.GONE);
                        //    mapsback.setVisibility(View.GONE);
                        openFragment(FriendsFragment.newInstance());
                        return true;
                    case R.id.navigation_settings:
                        searchView.setVisibility(View.GONE);
                        spinner.setVisibility(View.GONE);
                        friendsbackground.setVisibility(View.GONE);
                        dangerzonebutton.setVisibility(View.GONE);
                        //   mapsback.setVisibility(View.GONE);
                        openFragment(SettingsActivity.newInstance());
//                        Intent intent = new Intent(MapsActivity.this,SecondSplashActivity.class);
//                        startActivityForResult(intent,2);
                        return true;
                    case R.id.navigation_maps:
                        // dangerzonebutton.setVisibility(View.VISIBLE);
                        //  switchbutton.setVisibility(View.VISIBLE);
                        //    searchView.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
                        startActivity(intent);
                        handler.removeCallbacksAndMessages(null);
                        finish();

                        return true;


                }
                return true;
            }
        });
        //rahul end


        decorView.setFocusableInTouchMode(true);
        decorView.requestFocus();
        decorView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            final public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    return true;
                }
                return false;
            }
        });


    }


    private LatLng eventlocation;

    public void setEventsLocation(LatLng ll, String mess) {
        eventlocation = ll;
        if ((mess.equals("Home Location")) && !(mess.equals(" "))) {
            mMap.addMarker(new MarkerOptions().position(eventlocation).title(mess).icon(BitmapDescriptorFactory.fromResource(R.drawable.home3))).setSnippet("All because of dale");


            //  moveToCurrentLocation(eventlocation);

        } else if (eventlocation != null) {
            mMap.addMarker(new MarkerOptions().position(eventlocation).title(mess)).setSnippet("All because of dale");
            // moveToCurrentLocation(eventlocation);
        }
    }

private ArrayList<Marker> friendsMarker;
    private int friendsMarkerCounter=0;
    public void setEventsLocationwithoutzooming(LatLng ll, String mess) {
        friendsMarker.add(mMap.addMarker(new MarkerOptions().position(ll).snippet("Contacts").icon(BitmapDescriptorFactory.fromResource(R.drawable.face2))));
        friendsMarker.get(friendsMarkerCounter).setTag(mess);
        friendsMarkerCounter++;
    }

    public void setEventsLocationwithoutzoomingwithdesc(LatLng ll, String mess) {

     mMap.addMarker(new MarkerOptions().position(ll).snippet("Events").icon(BitmapDescriptorFactory.fromResource(R.drawable.event3))).setTag(mess);;

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private List<UserLocation> userList;

    private void readUsers() {

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userLocation");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserLocation user = snapshot.getValue(UserLocation.class);
                    assert user != null;
                    assert fUser != null;
                    if (!user.getUID().equals(fUser.getUid())) {
                        userList.add(user);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setcontactslocation() {
        if(friendsMarkerCounter!=0) {
            for (final Marker friendMarkers : friendsMarker) {
                 friendMarkers.remove();
            }
        }
        friendsMarker=new ArrayList<Marker>();
        friendsMarkerCounter=0;
//        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
//        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friends");

       for(final String fuid : eventFriends){
           for (final UserLocation ul : userList) {

                                if (fuid.equals(ul.getUID())) {
                                    LatLng ll = new LatLng(ul.getLat(), ul.getLon());
                                    setEventsLocationwithoutzooming(ll, ul.getUID());
                                }
                    }
       }

//        databaseReference.orderByKey().equalTo(fUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(fUser.getUid()).exists()) {
//                    for (final UserLocation ul : userList) {
//
//                        databaseReference.child(fUser.getUid()).orderByKey()
//                                .equalTo(ul.getUID()).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.child(ul.getUID()).exists()) {
//                                    LatLng ll = new LatLng(ul.getLat(), ul.getLon());
//                                    setEventsLocationwithoutzooming(ll, ul.getUID());
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }


    public void setHomeLoc() {
        db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child(userID).child(Common.USER_ADDRESS).child(Common.HOME_LOC).exists())
                ) {
                    double eventlonitude = Double.parseDouble(dataSnapshot.child(userID)
                            .child(Common.USER_ADDRESS)
                            .child(Common.USER_HOME_LNG)
                            .getValue().toString());
                    double eventlatitude = Double.parseDouble(dataSnapshot.child(userID)
                            .child(Common.USER_ADDRESS)
                            .child(Common.USER_HOME_LAT)
                            .getValue().toString());
                    LatLng eventloc = new LatLng(eventlatitude, eventlonitude);
                    setEventsLocation(eventloc, "Home Location");
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                handler.removeCallbacksAndMessages(null);
            }
        });

    }

    private void setmaptoretro() {
        boolean success = mMap.setMapStyle(null);
    }

    private void setmaptonight() {
        boolean success = mMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
    }

    private String userID = "nope";
    private DatabaseReference user_information = FirebaseDatabase.getInstance().getReference("userLocation");
    DatabaseReference user_information2 = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);

    private LatLng currentmostlocation=new LatLng(0,0);

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
//
//            @Override
//            public void onMapClick(LatLng point) {
//friendsbackground.setVisibility(View.GONE);
//            }
//
//        });
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//               double  eventlonitude=Double.parseDouble(dataSnapshot.child("address").child("longitude").getValue().toString());
//                double eventlatitude=Double.parseDouble(dataSnapshot.child("address").child("latitude").getValue().toString());
//                String mess=dataSnapshot.child("message").getValue().toString()+", on "+dataSnapshot.child("date").getValue().toString();
//                LatLng eventloc=new LatLng(eventlatitude,eventlonitude);
//                setEventsLocation(eventloc,mess);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        db2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                double  eventlonitude=Double.parseDouble(dataSnapshot.child("address").child("longitude").getValue().toString());
//                double eventlatitude=Double.parseDouble(dataSnapshot.child("address").child("latitude").getValue().toString());
//                String mess=dataSnapshot.child("message").getValue().toString()+", on "+dataSnapshot.child("date").getValue().toString();
//                LatLng eventloc=new LatLng(eventlatitude,eventlonitude);
//                setEventsLocation(eventloc,mess);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        try {
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (checkPermissions() && firebaseUser.getUid() != null) {
                googleMap.setMyLocationEnabled(true);


                mMap = googleMap;
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(final Location location) {

                                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                                currentmostlocation=sydney;
//                            mMap.addMarker(new MarkerOptions().position(sydney).title("My Location").icon(BitmapDescriptorFactory
//                                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE ))).setSnippet("All because of dale");
                                moveToCurrentLocation(sydney);

                                user_information.orderByKey()
                                        .equalTo(firebaseUser.getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getValue() == null) {
                                                    //uid not exist
                                                    if (!dataSnapshot.child(firebaseUser.getUid()).exists()) {

                                                        Common.currentUser = new UserLocation(firebaseUser.getUid(), location.getLatitude(), location.getLongitude());
                                                        user_information.child(Common.currentUser.getUID())
                                                                .setValue(Common.currentUser);
                                                        userID = firebaseUser.getUid();
                                                    }
                                                }
                                                //if user available
                                                else {
                                                    userID = firebaseUser.getUid();
                                                    Common.currentUser = dataSnapshot.child(firebaseUser.getUid()).getValue(UserLocation.class);
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                final String imgURL = "default";
                                final String userName = "Custos";

                                user_information2.orderByKey()
                                        .equalTo(firebaseUser.getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getValue() == null) {
                                                    //uid not exist
                                                    if (!dataSnapshot.child(firebaseUser.getUid()).exists()) {
                                                        displayDialog();
                                                        Common.loggedUser = new User(userName,
                                                                firebaseUser.getUid(),
                                                                firebaseUser.getEmail());
                                                        user_information2.child(Common.loggedUser.getUID())
                                                                .setValue(Common.loggedUser);
                                                        FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)
                                                                .child(firebaseUser.getUid())
                                                                .child(Common.IMAGE_URL)
                                                                .setValue(imgURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(getApplicationContext(), "successfully saved imgurl", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "failed imgurl", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                                                    @Override
                                                                    public void onSuccess(InstanceIdResult instanceIdResult) {
//                                    tokens.child(firebaseUser.getUid())
//                                            .setValue(instanceIdResult.getToken());
                                                                        String deviceToken = instanceIdResult.getToken();
                                                                        user_information2.child(firebaseUser.getUid()).child("userToken").setValue(deviceToken)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                    }
                                                                                });
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(MapsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                                updateTokens(firebaseUser);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                if (location != null) {

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {

        }

        setlocationeveryfeesec(googleMap);
        darkModeChecker();
        eventLocationAdder();
        // Add a marker in Sydney and move the camera

        /**
         * Generate markers
         */
        dangerZoneFacilitator();
        //generateMarkers();

        //Dale Code
        mMap.setOnMarkerClickListener(this);

    }

    private void displayDialog() {
        FirstTimeLoginDialog firstTimeLoginDialog = new FirstTimeLoginDialog();
        firstTimeLoginDialog.show(getSupportFragmentManager(), "first login");
    }

    private void updateTokens(final FirebaseUser firebaseUser) {
        final DatabaseReference tokens = FirebaseDatabase.getInstance()
                .getReference(Common.TOKENS);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        tokens.child(firebaseUser.getUid())
                                .setValue(instanceIdResult.getToken());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MapsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    ArrayList<String> eventListSelection;
private boolean aretherenofriends=false;
    private void addEventsSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.mapsEventSelection);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, eventListSelection);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                eventFriends =new ArrayList<String>();
                final int index = arg0.getSelectedItemPosition();
//                Toast.makeText(getBaseContext(),
//                            "You have selected item : " + eventListSelection.get(index),
//                        Toast.LENGTH_SHORT).show();

                if(noeventschecker==0){


                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("user_event").child(firebaseUser.getUid()).child(eventListSelectionid.get(index));
                eventRef
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("invited_users").getValue().toString().equals("NONE")){
                                    aretherenofriends=true;
                                }else

                               if(dataSnapshot.child("isOwner").getValue().toString().equals("true")){
                                   aretherenofriends=false;
                                   for (DataSnapshot snapshot :  dataSnapshot.child("invited_users").getChildren()) {
                                        if(snapshot.child("status").getValue().toString().equals("accepted")){
                                            eventFriends.add(snapshot.getKey());
                                            FirebaseDatabase.getInstance().getReference("eventMessage").child(eventListSelectionid.get(index)).child(snapshot.getKey())
                                                    .setValue("No Status");

                                        }

                                   }


                               }else{
                                   aretherenofriends=false;
                                   for (DataSnapshot snapshot :  dataSnapshot.child("invited_users").getChildren()) {
                                        eventFriends.add(snapshot.getKey());
                                       FirebaseDatabase.getInstance().getReference("eventMessage").child(eventListSelectionid.get(index)).child(snapshot.getKey())
                                               .setValue("No Status");

                                   }

                               }
                               setcontactslocation();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }
    private int noeventschecker=0;
private ArrayList<String> eventFriends;
    private ArrayList<String> eventListSelectionid;
    private void eventLocationAdder() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("user_event");
        eventRef.orderByKey()
                .equalTo(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        eventListSelection = new ArrayList<String>();
                        eventListSelectionid= new ArrayList<String>();
                        if (dataSnapshot.getValue() == null) {
                            //uid not exist
                            if (!dataSnapshot.child(firebaseUser.getUid()).exists()) {
                                eventListSelection.add("NO EVENTS");
                                eventListSelectionid.add("NOPE");
                                noeventschecker=1;
                            }
                        }
                        //if user available
                        else {
                            for (DataSnapshot snapshot : dataSnapshot.child(firebaseUser.getUid()).getChildren()) {
                                eventListSelection.add(snapshot.child("name").getValue().toString());
                                eventListSelectionid.add(snapshot.getKey());
                                noeventschecker=0;
                                double lat = 0, lon = 0;
                                if (snapshot.child("location").exists() && snapshot.child("description").exists() && snapshot.child("date").exists() && snapshot.child("time").exists() && snapshot.child("name").exists()) {

                                    lat = Double.parseDouble(snapshot.child("location").child("latitude").getValue().toString());
                                    lon = Double.parseDouble(snapshot.child("location").child("longitude").getValue().toString());
                                    setEventsLocationwithoutzoomingwithdesc(new LatLng(lat, lon), snapshot.getKey());
                                }

                            }

                        }
                        addEventsSpinner();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    private void darkModeChecker() {
        final DatabaseReference darkLight = FirebaseDatabase.getInstance().getReference("userSettings");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        darkLight.orderByKey()
                .equalTo(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            //uid not exist
                            if (!dataSnapshot.child(firebaseUser.getUid()).exists()) {

                                darkLight.child(firebaseUser.getUid()).child("darkmode").setValue("true");
                                setmaptonight();

                            }
                        }
                        //if user available
                        else {
                            String darklightval = dataSnapshot.child(firebaseUser.getUid()).child("darkmode").getValue().toString();
                            if (darklightval.equals("true")) {
                                setmaptonight();
                            } else {
                                setmaptoretro();
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void getcurrentlocation(GoogleMap googleMap) {
        if (checkPermissions() && !userID.equals("nope")) {

            mMap = googleMap;
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(final Location location) {
                            FirebaseDatabase.getInstance().getReference("userLocation").child(userID).child("lat")
                                    .setValue(location.getLatitude());
                            FirebaseDatabase.getInstance().getReference("userLocation").child(userID).child("lon")
                                    .setValue(location.getLongitude());
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("MapDemoActivity", "Error trying to get last GPS location");
                    e.printStackTrace();
                }
            });
        }
    }

    final Handler handler = new Handler();

    private void setlocationeveryfeesec(final GoogleMap googleMap) {
        readUsers();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getcurrentlocation(googleMap);
                try {
                    readUsers();
                  //  setcontactslocation();
                } catch (Exception e) {
                }
                handler.postDelayed(this, 10000);
            }
        }, 10000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setHomeLoc();
                try {
                  //  setcontactslocation();
                } catch (Exception e) {
                }
            }
        }, 2000);
        //  handler.postDelayed(new Runnable() {
        //   @Override
        //    public void run() {
        // GifImageView gifimg=findViewById(R.id.gifinmaps);
        //  gifimg.setVisibility(View.GONE);
        //    final Button dangerzonebutton= findViewById(R.id.mapsDsngerZoneButton);
        //    dangerzonebutton.setVisibility(View.VISIBLE);
        //    }
        //  }, 2500);
    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            // Here, thisActivity is the current activity
            requestPermissions();
            return false;
        }


    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0
                );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ok: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent openApp = getPackageManager().getLaunchIntentForPackage("com.example.custos");

                    startActivity(openApp);
                    System.out.println("GRANTED");
                } else {
                    System.out.println("Not GRANTED");
                    System.exit(0);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    /**
     * THIS IS HOW WE INTERACT WITH THE MAP
     * LINK: https://www.javatpoint.com/android-startactivityforresult-example?fbclid=IwAR0jMGAHYTVHMJVogObT-lkYwkyRWjMbTlVOLjJYL1B1i-TSfgFRxuyy7S4
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: Add in the lat and latitude coordinates as well as description into the database

        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2

        //High Danger Marker Code
        if (requestCode == 2) {
//            requestCode = data.getIntExtra("dangervalue",0);
//            String criticallevel = data.getStringExtra("criticallevel");
//            String description = data.getStringExtra("dangerdescription");
//            //TODO: Add marker at current location
//
//            LatLng stateCollege = new LatLng(40.7934,-77.86);
//            MarkerOptions dangerMarker = new MarkerOptions().position(stateCollege).title(criticallevel).icon(BitmapDescriptorFactory.fromResource(R.drawable.redtriangle));
//            dangerMarker.snippet(description);
//
//            mMap.addMarker(dangerMarker);
//            moveToCurrentLocation(stateCollege);
//
//
//            System.out.println("This MAP ish workin");
        }

        //Medium Danger Value
        if (requestCode == 3) {
//            requestCode = data.getIntExtra("dangervalue",0);
//            String criticallevel = data.getStringExtra("criticallevel");
//            String description = data.getStringExtra("dangerdescription");
//            //TODO: Add marker at current location
//
//            LatLng desmoines = new LatLng(41.619,-93.598);
//            MarkerOptions dangerMarker = new MarkerOptions().position(desmoines).title(criticallevel).icon(BitmapDescriptorFactory.fromResource(R.drawable.orangetriangle));
//            dangerMarker.snippet(description);
//
//            mMap.addMarker(dangerMarker);
//            moveToCurrentLocation(desmoines);

        }

        //Low Danger Value
        if (requestCode == 4) {
//            requestCode = data.getIntExtra("dangervalue",0);
//            String criticallevel = data.getStringExtra("criticallevel");
//            String description = data.getStringExtra("dangerdescription");
//            //TODO: Add marker at current location
//
//            LatLng hershey = new LatLng(40.2859,-76.658);
//            MarkerOptions dangerMarker = new MarkerOptions().position(hershey).title(criticallevel).icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowtriangle));
//            dangerMarker.snippet(description);
//
//            mMap.addMarker(dangerMarker);
//            moveToCurrentLocation(hershey);

        }


    }

    private String getInvitedUsers(String data) {
        //I/System.out: {a={name=Madison Beer}, b={name=Blake Lively}, c={name=Alex Morgan}}
        String invited_users = "";

        if (data == "none") {
            return "none";
        } else {
            for (int i = 0; i < data.length(); i++) {
                if (i + 5 < data.length()) {
                    if (data.substring(i, i + 5).equals("name=")) {
                        int index = i + 5;
                        for (int j = index; j < data.length(); j++) {
                            if (data.charAt(j) == '}') {
                                invited_users += (data.substring(index, j) + ',');
                                break;
                            }
                        }
                    }
                }
            }
            return invited_users;
        }
    }

    /**
     * This is the actionlistener for all the markers!! Add to this method if you want marker
     * to do something when clicked.
     * @param marker
     * @return
     */
    JSONArray data2;

    @Override
    public boolean onMarkerClick(final Marker marker) {
        final RelativeLayout mapsfriendlayoutbackgorund = findViewById(R.id.mapsBackground);
      try{
        if (marker.getSnippet().contains("Events")) {
            final TextView mapseventName = findViewById(R.id.mapseventName);
            final Button mapeventbutton = findViewById(R.id.mapseventbutton);
            final Button mapgetdirectionbutton = findViewById(R.id.mapsgetdirectionbutton);
            final RelativeLayout mapseventlayout = findViewById(R.id.mapseventzone);
            final String eventid = marker.getTag().toString();






            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            db = FirebaseDatabase.getInstance().getReference("user_event").child(firebaseUser.getUid()).child(eventid);
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    data2 = new JSONArray();
                    JSONObject obj = new JSONObject();
                    try {
                        mapgetdirectionbutton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Uri gmmIntentUri = Uri.parse("google.navigation:q="+dataSnapshot.child("location").child("latitude").getValue().toString()+","+dataSnapshot.child("location").child("longitude").getValue().toString());
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        });
                        obj.put("id", dataSnapshot.getKey());
                        obj.put("name", dataSnapshot.child("name").getValue());
                        obj.put("location", dataSnapshot.child("area").getValue());
                        obj.put("date", dataSnapshot.child("date").getValue());
                        obj.put("time", dataSnapshot.child("time").getValue());
                        obj.put("description", dataSnapshot.child("description").getValue());
                        obj.put("location_name", dataSnapshot.child("location_name").getValue());
                        if (dataSnapshot.child("invited_users").getValue() == null) {
                            obj.put("invited_users", "none");
                        } else {
                            obj.put("invited_users", getInvitedUsers(dataSnapshot.child("invited_users").getValue().toString()));
                        }
                    } catch (JSONException e) {
                        marker.remove();
                        e.printStackTrace();
                    }
                    data2.put(obj);
                    try {
                        mapseventName.setText(data2.getJSONObject(0).getString("name"));
                    } catch (Exception e) {
                        marker.remove();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    marker.remove();
                }
            });

            mapseventName.setVisibility(View.VISIBLE);
            mapseventlayout.setVisibility(View.VISIBLE);
            mapeventbutton.setVisibility(View.VISIBLE);
            mapsfriendlayoutbackgorund.setVisibility(View.VISIBLE);
            mapeventbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    mapseventlayout.setVisibility(View.GONE);
                    mapsfriendlayoutbackgorund.setVisibility(View.GONE);
                    mapeventbutton.setVisibility(View.GONE);
                    mapseventName.setVisibility(View.GONE);
                    try {
                        Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
                        intent.putExtra("event_id", data2.getJSONObject(0).getString("id"));
                        intent.putExtra("event_name", data2.getJSONObject(0).getString("name"));
                        intent.putExtra("event_desc", data2.getJSONObject(0).getString("description"));
                        intent.putExtra("event_date", data2.getJSONObject(0).getString("date"));
                        intent.putExtra("event_time", data2.getJSONObject(0).getString("time"));
                        intent.putExtra("invited_users", data2.getJSONObject(0).getString("invited_users"));
                        intent.putExtra("location_name", data2.getJSONObject(0).getString("location_name"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        System.out.println(e);
                    }
                }
            });

        }}catch (Exception e){
          marker.remove();
      }

        if (marker.getSnippet().contains("Contacts")) {
            final TextView mapsfriendName = findViewById(R.id.mapsfriendName);

            final Button friendmapbutton = findViewById(R.id.mapsfriendbutton);
            final RelativeLayout mapsfriendlayout = findViewById(R.id.mapsfriendzone);
            final CircleImageView mapsfriendicon = findViewById(R.id.mapsfriendimage);

            //    final FirebaseStorage storage = FirebaseStorage.getInstance();
            final String uidtemp = marker.getTag().toString();


            user_information2.child(uidtemp).orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mapsfriendName.setText(dataSnapshot.child("userName").getValue().toString());
                    String imgurl = dataSnapshot.child("imageURL").getValue().toString();
                    if (imgurl.equals("default")) {
                        mapsfriendicon.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        //  StorageReference httpsReference = storage.getReferenceFromUrl(imgurl);
                        Glide.with(getApplicationContext()).load(imgurl).into(mapsfriendicon);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            mapsfriendName.setVisibility(View.VISIBLE);

            mapsfriendlayout.setVisibility(View.VISIBLE);
            friendmapbutton.setVisibility(View.VISIBLE);
            mapsfriendlayoutbackgorund.setVisibility(View.VISIBLE);

            friendmapbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    mapsfriendlayout.setVisibility(View.GONE);
                    mapsfriendlayoutbackgorund.setVisibility(View.GONE);
                    friendmapbutton.setVisibility(View.GONE);
                    Intent intent = new Intent(MapsActivity.this, OtherUserActivity.class);
                    intent.putExtra("userid", uidtemp);
                    startActivity(intent);
                }
            });


        }

        /**
         * Dale's danger zone markers
         */

        if (marker.getSnippet().contains("Danger")) {
            String criticalLevel = marker.getTitle();
            String description = marker.getSnippet();
            DangerZoneDialogue dangerZoneDialogue = new DangerZoneDialogue();

            Bundle args = new Bundle();
            //Set arguments in the bundle
            args.putString("criticallevel", criticalLevel);
            args.putString("description", description);
            dangerZoneDialogue.setArguments(args);
            dangerZoneDialogue.show(getSupportFragmentManager(), "danger zone dialogue");
            //dangerZoneDialogue.setStyle(0,Style.);
        }
        return false;
    }

    /**
     * Getting all the markers in the DB and running the placeMarker method
     */
    public void generateMarkers(){
        /**
         * State verification
         */
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double currentlongitude = location.getLongitude();
        final double currentlatitude = location.getLatitude();

        final String usersCurrentState = getCurrentState(currentlatitude,currentlongitude);
        System.out.println("CURRENT STATE IS: " + usersCurrentState);


        FirebaseDatabase.getInstance().getReference().child("Danger Zone Markers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int numberOfMarkers = 0;
                        int displayedMarkers = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            numberOfMarkers++;
                            String zone_name = snapshot.child("zone_name").getValue().toString();
                            String risk_level = snapshot.child("risk_level").getValue().toString();
                            String lat = snapshot.child("lat").getValue().toString();
                            String longitude = snapshot.child("long").getValue().toString();
                            String description = snapshot.child("description").getValue().toString();

                            String dangerZoneState = getCurrentState(Double.valueOf(lat),Double.valueOf(longitude));
                            System.out.println("DANGER ZONE STATE: " + dangerZoneState);
                            if(dangerZoneState.equals(usersCurrentState)) {
                                displayedMarkers++;

                                placeMarker(zone_name,risk_level,lat,longitude,description);


                            }
                            //placeMarker(zone_name,risk_level,lat,longitude,description);
                            System.out.println(lat);
                        }
                        System.out.println("NUMBER OF TOTAL MARKERS: " + numberOfMarkers);
                        System.out.println("NUMBER OF DISPLAYED MARKERS: " + displayedMarkers);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    /**
     * retrieve the current state based off coordinates
     */
    private String getCurrentState(double lat, double longitude) {
        String currentState = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat,longitude,1);
            currentState = addresses.get(0).getAdminArea();
        } catch(IOException e){
            e.printStackTrace();
        }
        return currentState;
    }

    /**
     * Check to see if dangerZone is enabled or disabled
     * @return
     */
    private void dangerZoneFacilitator(){
        final DatabaseReference dangerZoneReference = FirebaseDatabase.getInstance().getReference("userSettings");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        boolean showDangerZone = true;
        dangerZoneReference.orderByKey()
                .equalTo(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                        if (dataSnapshot.getValue() == null) {
                            //uid not exist
                            if (!dataSnapshot.child(firebaseUser.getUid()).exists()) {


                            }
                        }
                        //if user available
                        else if(dataSnapshot.child(firebaseUser.getUid()).child("dangerzone").exists()){
                            String dangerzoneval=  dataSnapshot.child(firebaseUser.getUid()).child("dangerzone").getValue().toString();
                            if(dangerzoneval.equals("true")){
                                generateMarkers();
                            }else {
                                //DO NOTHING
                            }
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    /**
     * Actually placing the marker on the map based on what's in the DB
     * @param zone_name
     * @param risk_level
     * @param latitude
     * @param longitude
     * @param description
     */
    public void placeMarker(String zone_name,String risk_level, String latitude, String longitude,String description){
        double latitudeNumericVal = Double.valueOf(latitude);
        double longitudeNumericVal = Double.valueOf(longitude);

        LatLng coordinates = new LatLng(latitudeNumericVal,longitudeNumericVal);
        System.out.println("RISK LEVELS ARE: " + risk_level);
        if(risk_level.contains("Low")){


            // Instantiating CircleOptions to draw a circle around the marker
            CircleOptions circleOptions = new CircleOptions();

            // Specifying the center of the circle
            circleOptions.center(coordinates);

            // Radius of the circle
            circleOptions.radius(250);

            // Border color of the circle
            circleOptions.strokeColor(Color.parseColor("#e0ee20"));

            // Fill color of the circle
            circleOptions.fillColor(Color.parseColor("#20e0ee20"));



            // Border width of the circle
            circleOptions.strokeWidth(3f);


           // circleOptions.clickable(true);

            // Adding the circle to the GoogleMap
            mMap.addCircle(circleOptions);



            final MarkerOptions dangerMarker = new MarkerOptions().position(coordinates).title(zone_name);

            //    MarkerOptions dangerMarker = new MarkerOptions().position(coordinates).title(zone_name).icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowradius));

            dangerMarker.snippet("Low Danger:\n" + description);

            mMap.addMarker(dangerMarker);

        }

        if (risk_level.contains("Medium")) {


            CircleOptions circleOptions = new CircleOptions();

            // Specifying the center of the circle
            circleOptions.center(coordinates);

            // Radius of the circle
            circleOptions.radius(500);

            // Border color of the circle
            circleOptions.strokeColor(Color.parseColor("#FF9700"));

            // Fill color of the circle
            circleOptions.fillColor(Color.parseColor("#20FF9700"));

            // Border width of the circle
            circleOptions.strokeWidth(3f);


            // Adding the circle to the GoogleMap
            mMap.addCircle(circleOptions);




            MarkerOptions dangerMarker = new MarkerOptions().position(coordinates);

            dangerMarker.snippet("Medium Danger:\n" + description);
           // dangerMarker.visible(false);
            mMap.addMarker(dangerMarker);
        }

        if(risk_level.contains("High")) {
            CircleOptions circleOptions = new CircleOptions();

            // Specifying the center of the circle
            circleOptions.center(coordinates);

            // Radius of the circle
            circleOptions.radius(750);

            // Border color of the circle
            circleOptions.strokeColor(Color.parseColor("#cc0000"));

            // Fill color of the circle
            circleOptions.fillColor(Color.parseColor("#20cc0000"));

            // Border width of the circle
            circleOptions.strokeWidth(3f);


            // Adding the circle to the GoogleMap
            mMap.addCircle(circleOptions);




            MarkerOptions dangerMarker = new MarkerOptions().position(coordinates);

            dangerMarker.snippet("High Danger:\n" + description);

            mMap.addMarker(dangerMarker);
        }

    }

}
