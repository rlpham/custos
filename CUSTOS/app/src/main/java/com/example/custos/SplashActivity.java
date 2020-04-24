package com.example.custos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.custos.utils.Common;
import com.example.custos.utils.SignInDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import afu.org.checkerframework.checker.igj.qual.I;

public class SplashActivity extends AppCompatActivity {
    Button signInButton;
    GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN = 0;
    TextView termsNService;
    FirebaseAuth mAuth;
    User userApp = new User();
    boolean first, second;
    private SharedPreference sharedPreferenceObj;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

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

        termsNService = findViewById(R.id.termNSer);
        termsNService.setVisibility(View.INVISIBLE);
        termsNService.setText(Html.fromHtml("Do you agree to the \n<a href=\'https://github.com/rlpham/Custos/blob/master/END%20USER%20LICENSE%20AGREEMENT.pdf\'>Terms and Service</a>?"));
        termsNService.setMovementMethod(LinkMovementMethod.getInstance());
        ////////////////////https://github.com/rlpham/Custos/blob/master/PrivacyPolicy.pdf

        sharedPreferenceObj = new SharedPreference(SplashActivity.this);
        if (sharedPreferenceObj.getApp_runFirst().equals("FIRST")) {
            final SpannableString s = new SpannableString("https://github.com/rlpham/Custos/blob/master/TermsAndServices.pdf"); // msg should have url to enable clicking
            Linkify.addLinks(s, Linkify.ALL);

            final AlertDialog d = new AlertDialog.Builder(SplashActivity.this, R.style.Chill)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setSecond();
                            if (first && second) {

                                sharedPreferenceObj.setApp_runFirst("NO");
                                System.out.println("no longer first time");
                            }
                        }
                    })
                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();

                        }
                    })
                    .setIcon(R.drawable.ic_error_yellow_24dp)
                    .setTitle("Terms and Services")
                    .setMessage(Html.fromHtml("Do you accept Custos's " + "<a href=\"https://github.com/rlpham/Custos/blob/master/TermsAndServices.pdf\">Terms & Services</a>?"))
                    .create();

            d.show();
            ((TextView) d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());


//            final SpannableString st = new SpannableString("https://github.com/rlpham/Custos/blob/master/PrivacyPolicy.pdf"); // msg should have url to enable clicking
//            Linkify.addLinks(st, Linkify.ALL);

            final AlertDialog dd = new AlertDialog.Builder(SplashActivity.this, R.style.Chill)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //sharedPreferenceObj.setApp_runFirst("NO");
                            setFirst();
                            if (first && second) {

                                sharedPreferenceObj.setApp_runFirst("NO");
                                System.out.println("no longer first time");
                            }
                        }
                    })
                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setIcon(R.drawable.ic_error_yellow_24dp)
                    .setTitle("Privacy Policy")
                    .setMessage(Html.fromHtml("Do you accept Custos's " + "<a href=\"https://github.com/rlpham/Custos/blob/master/PrivacyPolicy.pdf\">Privacy Policy</a>?"))
                    .create();

            dd.show();
            ((TextView) dd.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

        } else {

        }


        if (checkPermissions() != true) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);


        }


        signInButton = findViewById(R.id.google_login);
        mAuth = FirebaseAuth.getInstance();
        createRequest();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermissions() != true) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, R.style.Chill);

                    builder.setTitle("Allow Access");
                    builder.setIcon(R.drawable.ic_error_yellow_24dp);
                    builder.setCancelable(false);
                    builder.setMessage("Please allow Custos to have location access")
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    builder.create();
                    builder.show();
                } else {
                    signIn();
                }


            }
        });
        //.check() not working

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            updateUI(firebaseUser);
            userApp.setUID(mAuth.getCurrentUser().getUid());
        }

    }

    private void createRequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }


    final Handler handler = new Handler();

    private void signIn() {


        final SignInDialog signInDialog = new SignInDialog(SplashActivity.this);
        signInDialog.startDialog();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                signInDialog.dismissDialog();
            }
        }, 2000);


//        startActivityForResult(
//                AuthUI.getInstance()
//                .createSignInIntentBuilder().setAvailableProviders(provider)
//                .build(),MY_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            final SignInDialog signInDialog = new SignInDialog(SplashActivity.this);
            signInDialog.startDialog();
            //The task returned from this call is always completed no need to attach a listener
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    signInDialog.dismissDialog();
                    fireBaseGoogleAuth(account);
                }
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SplashActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUI() {
        startActivity(new Intent(SplashActivity.this, MapsActivity.class));
        finish();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(SplashActivity.this, "Signin Successful!", Toast.LENGTH_SHORT).show();
            fireBaseGoogleAuth(account);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
            }, 4000);

            account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
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
                        if (task.isSuccessful()) {
                            Toast.makeText(SplashActivity.this, "Successful Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SplashActivity.this, "Failed Save", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference("User Account by Email").child("UID").child("userEmail")
                        .setValue(userApp.getUserEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(SplashActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SplashActivity.this, "Failed Save", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference("User Account by Email").child("UID").child("userId")
                        .setValue(userApp.getUserId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(SplashActivity.this,"Successful Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SplashActivity.this, "Failed Save", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        } catch (Exception e) {
            //the ApiException status code indicates the detailed failure reason
            //Please refer to the googlesigninstatuscodes class reference for more info
            //Log.w("Error", "signInResult:failed code =" + e.getStatusCode());
            Toast.makeText(SplashActivity.this, "Signin Failed!", Toast.LENGTH_SHORT).show();
            fireBaseGoogleAuth(null);
            Log.getStackTraceString(e);
        }
    }

    private void fireBaseGoogleAuth(GoogleSignInAccount account) {
        final SignInDialog signInDialog = new SignInDialog(SplashActivity.this);
        signInDialog.startDialog();
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(SplashActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                    final FirebaseUser user = mAuth.getCurrentUser();
                    String online_uid = mAuth.getCurrentUser().getUid();
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
//                                    tokens.child(firebaseUser.getUid())
//                                            .setValue(instanceIdResult.getToken());
                                    String deviceToken = instanceIdResult.getToken();
                                    userRef.child(user.getUid()).child("userToken").setValue(deviceToken)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                                    startActivity(intent);
                                                    updateUI(user);
                                                    signInDialog.dismissDialog();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SplashActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    Toast.makeText(SplashActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                    signInDialog.dismissDialog();
                }
            }
        });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            userApp.setUserEmail(firebaseUser.getEmail());
            userApp.setUID(firebaseUser.getUid());
        } else {
            userApp.setUserEmail(null);
            userApp.setUID(null);
        }
    }

    public void setFirst() {
        first = true;
    }

    public void setSecond() {
        second = true;
    }

//TODO: saved last signed in google account
//    @Override
//    protected void onStart(){
//        super.onStart();
//        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
//
//    }
}