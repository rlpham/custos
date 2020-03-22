package com.example.custos;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecondSplashActivity extends AppCompatActivity {
    ImageView imageView;
    TextView name, email, id, homeLocation;
    Button signOut,setHomeButton;
    GoogleSignInClient googleSignInClient;
    List<Address> addresses=new ArrayList<>();
    Geocoder geocoder;
    SetHomeLocation setHomeLocation = new SetHomeLocation();
    DBHandler db = new DBHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_splash_activity);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        imageView =     findViewById(R.id.imageView);
        name =          findViewById(R.id.textName);
        email =         findViewById(R.id.textEmail);
        id =            findViewById(R.id.textId);
        signOut =       findViewById(R.id.signout_button);
        homeLocation =  findViewById(R.id.homeLocation);
        setHomeButton = findViewById(R.id.setHomeLocation);
        stringAddress(setHomeLocation.getLatitude(),setHomeLocation.getLongtitude());

        signOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.signout_button:
                        signOut();
                        break;
                }
            }
        });

        setHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondSplashActivity.this,SetHomeLocationActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            String personID = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            name.setText(personName);
            email.setText(personEmail);
            id.setText(personID);
            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
        }
    }
    private void stringAddress(double latitude, double longitude){
        if(!addresses.isEmpty()){
            try {
                addresses = geocoder.getFromLocation(latitude,longitude,1);
                if(addresses.size()>0){
                    String myAddress = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String fullAddress = myAddress + " " + city + " " + state;
                    homeLocation.setText(fullAddress);
                    System.out.println("---------------------------------"+myAddress + city);
                    Log.i("test","address: "+ myAddress + city + state);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void signOut(){
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(SecondSplashActivity.this,SplashActivity.class);
                        startActivity(intent);
                        Toast.makeText(SecondSplashActivity.this,"Sign out successfully",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

    }
}
