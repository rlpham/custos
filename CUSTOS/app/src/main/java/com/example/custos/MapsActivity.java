package com.example.custos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private DatabaseReference db,db2,db3,db4;
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
                if(location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
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



        final Button dangerzonebutton= findViewById(R.id.mapsDsngerZoneButton);
        dangerzonebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,DangerZoneActivity.class);
                int requestCode = intent.getIntExtra("dangervalue",3);
                //startActivityForResult(intent,requestCode);
                startActivityForResult(intent,requestCode);
                //startActivity(intent);
            }
        });
        final Button friendmapbutton=findViewById(R.id.mapsfriendbutton);
        final LinearLayout mapsback=findViewById(R.id.mapsbackground);
        final LinearLayout friendsbackground=findViewById(R.id.mapsfriendzone);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_maps);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_events:
                        dangerzonebutton.setVisibility(View.GONE);
                        searchView.setVisibility(View.GONE);
                      //  friendmapbutton.setVisibility(View.GONE);
                    //    mapsback.setVisibility(View.GONE);
                        friendsbackground.setVisibility(View.GONE);
                        openFragment(MainEventListActivity.newInstance());
                        return true;
                    case R.id.navigation_notifications:
                        dangerzonebutton.setVisibility(View.GONE);
                        searchView.setVisibility(View.GONE);
                        friendsbackground.setVisibility(View.GONE);
                        openFragment(NotificationActivity.newInstance());
                        return true;
                    case R.id.navigation_friends:
                        dangerzonebutton.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                        friendsbackground.setVisibility(View.GONE);
                      openFragment(FriendsFragment.newInstance());
                        return true;
                    case R.id.navigation_settings:
                        searchView.setVisibility(View.GONE);
                        friendsbackground.setVisibility(View.GONE);
                        dangerzonebutton.setVisibility(View.GONE);
                        openFragment(SettingsActivity.newInstance());
//                        Intent intent = new Intent(MapsActivity.this,SecondSplashActivity.class);
//                        startActivityForResult(intent,2);
                        return true;
                    case R.id.navigation_maps:
                       // dangerzonebutton.setVisibility(View.VISIBLE);
                      //  switchbutton.setVisibility(View.VISIBLE);
                    //    searchView.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(MapsActivity.this,MapsActivity.class);
                        startActivity(intent);
                        finish();

                        return true;


                }
                return true;
            }
        });
        //rahul end




    }


private LatLng eventlocation;
    public void setEventsLocation(LatLng ll,String mess){
        eventlocation=ll;
        if((mess.equals("Home Location")) && !(mess.equals(" "))){
            mMap.addMarker( new MarkerOptions().position(eventlocation).title(mess).icon(BitmapDescriptorFactory.fromResource(R.drawable.home3))).setSnippet("All because of dale");



          //  moveToCurrentLocation(eventlocation);

        }else
        if(eventlocation!=null) {
            mMap.addMarker(new MarkerOptions().position(eventlocation).title(mess)).setSnippet("All because of dale");
           // moveToCurrentLocation(eventlocation);
        }
    }


    public void setEventsLocationwithoutzooming(LatLng ll,String mess) {


        mMap.addMarker(new MarkerOptions().position(ll).snippet("Contacts").icon(BitmapDescriptorFactory.fromResource(R.drawable.face2))).setTag(mess);

    }
    public void setEventsLocationwithoutzoomingwithdesc(LatLng ll,String mess,String desc) {


        mMap.addMarker(new MarkerOptions().position(ll).title(mess).icon(BitmapDescriptorFactory.fromResource(R.drawable.event3))).setSnippet(desc);

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
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UserLocation user = snapshot.getValue(UserLocation.class);
                        assert user != null;
                        assert fUser != null;
                        if(!user.getUID().equals(fUser.getUid())){
                            userList.add(user);
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

private void setcontactslocation(){
    final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friends");

    databaseReference.orderByKey().equalTo(fUser.getUid()).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.child(fUser.getUid()).exists()){
                for (final UserLocation ul: userList) {

                    databaseReference.child(fUser.getUid()).orderByKey()
                            .equalTo(ul.getUID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(ul.getUID()).exists()) {
                                LatLng ll = new LatLng(ul.getLat(), ul.getLon());
                                setEventsLocationwithoutzooming(ll, ul.getUID());

                            }
                           }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }

        }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});


}







    public void setHomeLoc(){
        db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.child(userID).child(Common.USER_ADDRESS).child(Common.HOME_LOC).exists())
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
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                handler.removeCallbacksAndMessages(null);
            }
        });

    }

    private void setmaptoretro(){
        boolean success = mMap.setMapStyle(null);
    }
    private void setmaptonight(){
        boolean success = mMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
    }

    private String userID="nope";
    private DatabaseReference user_information = FirebaseDatabase.getInstance().getReference("userLocation");
    DatabaseReference user_information2 = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);

    @Override
    public void onMapReady(GoogleMap googleMap) {


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
        if (checkPermissions()&&firebaseUser.getUid()!=null) {
            googleMap.setMyLocationEnabled(true);


            mMap = googleMap;
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(final Location location) {

                            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
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

                                                    Common.currentUser = new UserLocation(firebaseUser.getUid(),location.getLatitude() ,location.getLongitude() );
                                                    user_information.child(Common.currentUser.getUID())
                                                            .setValue(Common.currentUser);
                                                    userID=firebaseUser.getUid();
                                                }
                                            }
                                            //if user available
                                            else {
                                                userID=firebaseUser.getUid();
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
                                    if(dataSnapshot.getValue() == null){
                                        //uid not exist
                                        if(!dataSnapshot.child(firebaseUser.getUid()).exists()){
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
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(),"successfully saved imgurl",Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(getApplicationContext(),"failed imgurl",Toast.LENGTH_SHORT).show();
                                                    }
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
        }}
        catch (Exception e){

        }

        setlocationeveryfeesec(googleMap);
        darkModeChecker();
        eventLocationAdder();
        // Add a marker in Sydney and move the camera

        /**
         * Generate markers
         */
        generateMarkers();

        //Dale Code
        mMap.setOnMarkerClickListener(this);

    }

    private void displayDialog() {
        FirstTimeLoginDialog firstTimeLoginDialog = new FirstTimeLoginDialog();
        firstTimeLoginDialog.show(getSupportFragmentManager(),"first login");
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
                Toast.makeText(MapsActivity.this,"" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eventLocationAdder(){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("user_event");
        eventRef.orderByKey()
                .equalTo(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            //uid not exist
                            if (!dataSnapshot.child(firebaseUser.getUid()).exists()) {

                            }
                        }
                        //if user available
                        else {
                            for(DataSnapshot snapshot : dataSnapshot.child(firebaseUser.getUid()).getChildren()){
                                String name="",desc="",date="",time="";
                                double lat=0,lon=0;
                                if(snapshot.child("location").exists()&&snapshot.child("description").exists()&&snapshot.child("date").exists()&&snapshot.child("time").exists()&&snapshot.child("name").exists()) {
                                    name=snapshot.child("name").getValue().toString();
                                    lat = Double.parseDouble(snapshot.child("location").child("latitude").getValue().toString());
                                    lon = Double.parseDouble(snapshot.child("location").child("longitude").getValue().toString());
                                    desc=snapshot.child("description").getValue().toString();
                                    date=snapshot.child("date").getValue().toString();
                                    time=snapshot.child("time").getValue().toString();
                                    setEventsLocationwithoutzoomingwithdesc(new LatLng(lat,lon),name,desc +" at "+ time + " , "+date);
                                }

                            }
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


private void darkModeChecker(){
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
                        String darklightval=  dataSnapshot.child(firebaseUser.getUid()).child("darkmode").getValue().toString();
                        if(darklightval.equals("true")){
                            setmaptonight();
                        }else {
                            setmaptoretro();
                        }
                    }


                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

}
    private void getcurrentlocation(GoogleMap googleMap){
        if (checkPermissions()&&!userID.equals("nope")) {

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
    private void setlocationeveryfeesec(final GoogleMap googleMap){
        readUsers();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               getcurrentlocation(googleMap);
                handler.postDelayed(this, 10000);
            }
        }, 10000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               setHomeLoc();
                setcontactslocation();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //TODO: Add in the lat and latitude coordinates as well as description into the database

        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2

        //High Danger Marker Code
        if(requestCode==2)
        {
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
        if (requestCode == 3){
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
        if (requestCode == 4){
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

    /**
     * This is the actionlistener for all the markers!! Add to this method if you want marker
     * to do something when clicked.
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        /**
         * Dale's danger zone markers
         */
        if(marker.getSnippet().contains("Contacts")){
            final LinearLayout mapsfriendlayoutbackgorund=findViewById(R.id.mapsbackground);
            final Button friendmapbutton=findViewById(R.id.mapsfriendbutton);
            final LinearLayout mapsfriendlayout=findViewById(R.id.mapsfriendzone);
            final String uidtemp=marker.getTag().toString();
            mapsfriendlayoutbackgorund.setVisibility(View.VISIBLE);
            mapsfriendlayout.setVisibility(View.VISIBLE);
            friendmapbutton.setVisibility(View.VISIBLE);
            mapsfriendlayoutbackgorund.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                     mapsfriendlayout.setVisibility(View.GONE);
                     mapsfriendlayoutbackgorund.setVisibility(View.GONE);
                     friendmapbutton.setVisibility(View.GONE);
                }
            });

            friendmapbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mapsfriendlayout.setVisibility(View.GONE);
                    mapsfriendlayoutbackgorund.setVisibility(View.GONE);
                    friendmapbutton.setVisibility(View.GONE);
                    Intent intent = new Intent(MapsActivity.this, OtherUserActivity.class);
                    intent.putExtra("userid",uidtemp);
                    startActivity(intent);
                }
            });
         user_information2.child(uidtemp).orderByKey().addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}


        if(marker.getSnippet().contains("Danger")){
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

        FirebaseDatabase.getInstance().getReference().child("Danger Zone Markers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int numberOfMarkers = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            numberOfMarkers++;
                            String zone_name = snapshot.child("zone_name").getValue().toString();
                            String risk_level = snapshot.child("risk_level").getValue().toString();
                            String lat = snapshot.child("lat").getValue().toString();
                            String longitude = snapshot.child("long").getValue().toString();
                            String description = snapshot.child("description").getValue().toString();
                            placeMarker(zone_name,risk_level,lat,longitude,description);
                            System.out.println(lat);
                        }
                        System.out.println("NUMBER OF MARKERS" + numberOfMarkers);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
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
            MarkerOptions dangerMarker = new MarkerOptions().position(coordinates).title(zone_name).icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowtriangle));
            dangerMarker.snippet("Low Danger:\n" + description);

            mMap.addMarker(dangerMarker);
        }

        if (risk_level.contains("Medium")) {
            MarkerOptions dangerMarker = new MarkerOptions().position(coordinates).title(zone_name).icon(BitmapDescriptorFactory.fromResource(R.drawable.orangetriangle));
            dangerMarker.snippet("Medium Danger:\n" + description);

            mMap.addMarker(dangerMarker);
        }

        if(risk_level.contains("High")) {
            MarkerOptions dangerMarker = new MarkerOptions().position(coordinates).title(zone_name).icon(BitmapDescriptorFactory.fromResource(R.drawable.redtriangle));
            dangerMarker.snippet("High Danger:\n" + description);

            mMap.addMarker(dangerMarker);
        }

    }
}
