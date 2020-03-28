package com.example.custos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.custos.utils.Common;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    SignInButton signInButton;
   // Button signOutButton;
    GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN =0;
    private FirebaseAuth mAuth;
    User userApp = new User();
    DatabaseReference user_information;
    private static final int MY_REQUEST_CODE = 7773;
    List<AuthUI.IdpConfig> provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Paper.init(this);
        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
       // signOutButton = findViewById(R.id.signout_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.sign_in_button:
                        showSignInOptions();
                        break;
                }
            }
        });
//        signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                signOutButton.setVisibility(View.INVISIBLE);
//            }
//        });
        user_information = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
//        provider = Arrays.asList(
//                new AuthUI.IdpConfig.GoogleBuilder().build(),
//                new AuthUI.IdpConfig.EmailBuilder().build()
//        );
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        showSignInOptions();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(SplashActivity.this,"You must accept permission to use application",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                });
        //.check() not working
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
            googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        if(mAuth.getCurrentUser()!= null){
            userApp.setUID(mAuth.getCurrentUser().getUid());
        }
//        FirebaseDatabase.getInstance().getReference("User Account by Email").child("UID")
//                .setValue(userApp.getUID()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(SplashActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(SplashActivity.this,"Failed Save", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
    private void showSignInOptions(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,MY_REQUEST_CODE);
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(provider)
//                .build(),MY_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        //Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if(requestCode == MY_REQUEST_CODE){
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//            if(resultCode == RESULT_OK){
//                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                user_information.orderByKey()
//                        .equalTo(firebaseUser.getUid())
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if(dataSnapshot.getValue() == null){
//                                    //uid not exist
//                                    if(!dataSnapshot.child(firebaseUser.getUid()).exists()){
//                                        Common.loggedUser = new User(firebaseUser.getUid(),firebaseUser.getEmail(),firebaseUser.getDisplayName());
//                                        user_information.child(Common.loggedUser.getUID())
//                                                .setValue(Common.loggedUser);
//                                    }
//                                }
//                                //if user available
//                                else{
//                                    Common.loggedUser = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);
//                                }
//                                Paper.book().write(Common.USER_UID_SAVE_KEY,Common.loggedUser.getUID());
//                                updateToken(firebaseUser);
//                                setupUI();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//            }
            //The task returned from this call is always completed no need to attach a listener
           Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task,requestCode,resultCode,data);
        }



    private void updateToken(final FirebaseUser firebaseUser) {
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
                Toast.makeText(SplashActivity.this,"" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupUI(){
        startActivity(new Intent(SplashActivity.this,MapsActivity.class));
        finish();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask, int requestCode, int resultCode,Intent data) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(SplashActivity.this, "Signin Successful!", Toast.LENGTH_SHORT).show();
            fireBaseGoogleAuth(account);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent = new Intent(SplashActivity.this, MapsActivity.class);
            account = GoogleSignIn.getLastSignedInAccount(this);
            if (requestCode == MY_REQUEST_CODE) {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (resultCode == RESULT_OK) {
                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    user_information.orderByKey()
                            .equalTo(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                        //uid not exist
                                        if (!dataSnapshot.child(firebaseUser.getUid()).exists()) {
                                            Common.loggedUser = new User(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName());
                                            user_information.child(Common.loggedUser.getUID())
                                                    .setValue(Common.loggedUser);
                                        }
                                    }
                                    //if user available
                                    else {
                                        Common.loggedUser = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);
                                    }
                                    Paper.book().write(Common.USER_UID_SAVE_KEY, Common.loggedUser.getUID());
                                    updateToken(firebaseUser);
                                    setupUI();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
//                if (account != null) {
//                    String personName = account.getDisplayName();
//                    String personEmail = account.getEmail();
//                    String personID = account.getId();
//                    userApp.setUserName(personName);
//                    userApp.setUserEmail(personEmail);
//                    userApp.setUserId(personID);
//                    //TODO if(mAuth.getCurrentUser().getUid().equals())
//
//                    FirebaseDatabase.getInstance().getReference("User Account by Email").child("UID").child("userName")
//                            .setValue(userApp.getUserName()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(SplashActivity.this, "Successful Saved", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(SplashActivity.this, "Failed Save", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                    FirebaseDatabase.getInstance().getReference("User Account by Email").child("UID").child("userEmail")
//                            .setValue(userApp.getUserEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                //Toast.makeText(SplashActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(SplashActivity.this, "Failed Save", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                    FirebaseDatabase.getInstance().getReference("User Account by Email").child("UID").child("userId")
//                            .setValue(userApp.getUserId()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                //Toast.makeText(SplashActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(SplashActivity.this, "Failed Save", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                }
                startActivity(intent);
            }
        }catch(Exception e){
            //the ApiException status code indicates the detailed failure reason
            //Please refer to the googlesigninstatuscodes class reference for more info
            //Log.w("Error", "signInResult:failed code =" + e.getStatusCode());
            Toast.makeText(SplashActivity.this, "Signin Failed!", Toast.LENGTH_SHORT).show();
            fireBaseGoogleAuth(null);
            Log.getStackTraceString(e);
        }
    }

    private void fireBaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(SplashActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else{
                    Toast.makeText(SplashActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser firebaseUser){
        //signOutButton.setVisibility(View.VISIBLE);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(googleSignInAccount != null){
            String personName = googleSignInAccount.getDisplayName();
            String personGivenName = googleSignInAccount.getGivenName();
            String personFamilyName = googleSignInAccount.getFamilyName();
            String personEmail = googleSignInAccount.getEmail();
            String personId = googleSignInAccount.getId();
            Uri personPhoto = googleSignInAccount.getPhotoUrl();
            Toast.makeText(SplashActivity.this, "\t"+personName + "\n" + personEmail,Toast.LENGTH_SHORT).show();
        }
    }

//TODO: saved last signed in google account
//    @Override
//    protected void onStart(){
//        super.onStart();
//        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
//
//    }
}
