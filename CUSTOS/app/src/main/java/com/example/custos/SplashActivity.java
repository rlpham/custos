package com.example.custos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.custos.utils.Common;
import com.example.custos.utils.LoadingDialog;
import com.example.custos.utils.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SplashActivity extends AppCompatActivity {
    Button signInButton;
    GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN =0;
    FirebaseAuth mAuth;
    User userApp = new User();

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        signInButton = findViewById(R.id.google_login);
        mAuth = FirebaseAuth.getInstance();
        createRequest();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        //.check() not working

        if(mAuth.getCurrentUser()!= null){
            FirebaseUser firebaseUser =mAuth.getCurrentUser();
            updateUI(firebaseUser);
            userApp.setUID(mAuth.getCurrentUser().getUid());
        }
        FirebaseDatabase.getInstance().getReference("User Account by Email").child("UID")
                .setValue(userApp.getUID()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SplashActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SplashActivity.this,"Failed Save", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createRequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
    }


    final Handler handler = new Handler();
    private void signIn(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,RC_SIGN_IN);
            }
        },2000);



//        startActivityForResult(
//                AuthUI.getInstance()
//                .createSignInIntentBuilder().setAvailableProviders(provider)
//                .build(),MY_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        //Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if(requestCode == RC_SIGN_IN){

            //The task returned from this call is always completed no need to attach a listener
           Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
           try {
               GoogleSignInAccount account = task.getResult(ApiException.class);
               if(account != null){
                   fireBaseGoogleAuth(account);
               }
           }catch (ApiException e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
           }
            //handleSignInResult(task);
        }

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

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(SplashActivity.this,"Signin Successful!",Toast.LENGTH_SHORT).show();
            fireBaseGoogleAuth(account);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,MapsActivity.class);
                    startActivity(intent);
                }
            },4000);

            account = GoogleSignIn.getLastSignedInAccount(this);
            if(account != null){
                String personName = account.getDisplayName();
                String personEmail = account.getEmail();
                String personID = account.getId();
                userApp.setUserName(personName);
                userApp.setUserEmail(personEmail);
                userApp.setUserId(personID);
                //TODO if(mAuth.getCurrentUser().getUid().equals())
                FirebaseDatabase.getInstance().getReference("User Account by Email").child("UID").child("userName")
                        .setValue(userApp.getUserName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SplashActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SplashActivity.this,"Failed Save", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference("User Account by Email").child("UID").child("userEmail")
                        .setValue(userApp.getUserEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Toast.makeText(SplashActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SplashActivity.this,"Failed Save", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference("User Account by Email").child("UID").child("userId")
                        .setValue(userApp.getUserId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Toast.makeText(SplashActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SplashActivity.this,"Failed Save", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        }catch (Exception e){
            //the ApiException status code indicates the detailed failure reason
            //Please refer to the googlesigninstatuscodes class reference for more info
            //Log.w("Error", "signInResult:failed code =" + e.getStatusCode());
            Toast.makeText(SplashActivity.this,"Signin Failed!",Toast.LENGTH_SHORT).show();
            fireBaseGoogleAuth(null);
            Log.getStackTraceString(e);
        }
    }

    private void fireBaseGoogleAuth(GoogleSignInAccount account) {
        final LoadingDialog loadingDialog = new LoadingDialog(SplashActivity.this);
        loadingDialog.startLoadingDialog();
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(SplashActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                    startActivity(intent);
                    loadingDialog.dismissDialog();
                    updateUI(user);
                }else{
                    loadingDialog.dismissDialog();
                    Toast.makeText(SplashActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser firebaseUser){
        if(firebaseUser != null){
            userApp.setUserEmail(firebaseUser.getEmail());
            userApp.setUID(firebaseUser.getUid());
        }else{
            userApp.setUserEmail(null);
            userApp.setUID(null);
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