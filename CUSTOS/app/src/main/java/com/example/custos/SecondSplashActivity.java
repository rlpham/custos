package com.example.custos;

import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.custos.utils.Common;
import com.example.custos.utils.LoadingDialog;
import com.example.custos.utils.LogoutDialog;
import com.example.custos.utils.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondSplashActivity extends AppCompatActivity{
    CircleImageView imageView;
    TextView name,name2, email,email2,homeLocation,signOut,backButton;
    TextInputLayout phoneNum;
    TextView displayPhoneNumber,displayPIN,displayEmergencyContact;
    Button editUserInfo;
    GoogleSignInClient googleSignInClient;
    List<Address> addresses=new ArrayList<>();
    Geocoder geocoder;
    SetHomeLocation setHomeLocation = new SetHomeLocation();
    final Handler handler = new Handler();
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    //DBHandler db = new DBHandler();
    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseDatabase fdatabase;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_splash_activity);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        editUserInfo = findViewById(R.id.editUserinfo);
        imageView =     findViewById(R.id.imageView);
        name =          findViewById(R.id.textName);
        name2 =         findViewById(R.id.textName2);
        email =         findViewById(R.id.textEmail);
        email2 =         findViewById(R.id.textEmail2);
        phoneNum = findViewById(R.id.textPhoneNum);
        displayPhoneNumber = findViewById(R.id.textPhoneNumDisplay);
        displayPIN = findViewById(R.id.textPINdisplay);
        signOut =       findViewById(R.id.signout_button);
        homeLocation =  findViewById(R.id.homeLocation);
        displayEmergencyContact = findViewById(R.id.emergencyContactDisplay);

        //setHomeButton = findViewById(R.id.setHomeLocation);
        backButton =    findViewById(R.id.back_button2);
        storageReference = FirebaseStorage.getInstance().getReference(Common.IMAGE_UPLOAD);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION).child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openImage();
//            }
//        });
        stringAddress(setHomeLocation.getLatitude(),setHomeLocation.getLongtitude());

        editUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditUserInformation.class);
                Pair[] pairs = new Pair[11];
                pairs[0] = new Pair<View,String>(imageView,"profile_picture");
                pairs[1] = new Pair<View,String>(backButton,"back");
                pairs[2] = new Pair<View,String>(signOut,"logout");
                pairs[3] = new Pair<View,String>(name,"display_name");
                pairs[4] = new Pair<View,String>(email,"display_email");
                pairs[5] = new Pair<View,String>(name2,"full_name");
                pairs[6] = new Pair<View,String>(email2,"email");
                pairs[7] = new Pair<View,String>(displayPhoneNumber,"phone_number");
                pairs[8] = new Pair<View,String>(displayPIN,"pin");
                pairs[9] = new Pair<View,String>(homeLocation,"address");
                pairs[10] = new Pair<View,String>(editUserInfo,"edit_info");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SecondSplashActivity.this,pairs);
                    startActivity(intent,activityOptions.toBundle());
                }

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondSplashActivity.this,MapsActivity.class);
                startActivity(intent);

            }
        });
        signOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signOut();
            }
        });


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null || firebaseUser != null){
            String firebaseName = firebaseUser.getDisplayName();
            String firebaseEmail = firebaseUser.getEmail();
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            String personID = account.getId();
            Uri personPhoto = account.getPhotoUrl();
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
                        String fullAddress = dataSnapshot.child(firebaseUser.getUid())
                                .child(Common.USER_ADDRESS)
                                .child(Common.HOME_LOC)
                                .getValue().toString();
                        homeLocation.setText(fullAddress);
                    }else if((dataSnapshot.child(firebaseUser.getUid())
                            .child(Common.USER_ADDRESS)
                            .child(Common.HOME_LOC)
                            .exists()) && (dataSnapshot.child(firebaseUser.getUid())
                            .child(Common.USER_ADDRESS)
                            .child(Common.HOME_LOC)
                            .getValue()
                            .toString().equals(" "))){
                        homeLocation.setText("Something went wrong try again later!");
                    }
                    else {
                        homeLocation.setText("Home address is not set");
                    }

                 }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if((dataSnapshot.child(firebaseUser.getUid()).child(Common.USER_PHONE).exists())
                        && !(dataSnapshot.child(firebaseUser.getUid())
                            .child(Common.USER_PHONE).getValue().toString().equals(""))){
                        String phoneNumber = dataSnapshot.child(firebaseUser.getUid())
                                .child(Common.USER_PHONE).getValue().toString();
                        displayPhoneNumber.setText(phoneNumber);
                    }else{
                        displayPhoneNumber.setText("Phone number have not set");
                    }
                    if(dataSnapshot.child(firebaseUser.getUid()).child(Common.USER_NAME).exists()){
                        String userName = dataSnapshot.child(firebaseUser.getUid())
                                .child(Common.USER_NAME).getValue().toString();
                        name2.setText(userName);
                        name.setText(userName);
                    }
                    if(dataSnapshot.child(firebaseUser.getUid()).child(Common.USER_PIN).exists()
                            && !(dataSnapshot.child(firebaseUser.getUid())
                                .child(Common.USER_PIN).getValue().toString().equals(""))){
                        String pin =  dataSnapshot.child(firebaseUser.getUid())
                                .child(Common.USER_PIN).getValue().toString();
                        displayPIN.setText(pin);
                    }else{
                        displayPIN.setText("Please set your PIN");
                    }
                    if(dataSnapshot.child(firebaseUser.getUid()).child(Common.EMERGENCY_CONTACT).child(Common.EMERGENCY_NAME).exists()
                            && !(dataSnapshot.child(firebaseUser.getUid())
                                .child(Common.EMERGENCY_CONTACT).child(Common.EMERGENCY_NAME).getValue().toString().equals(""))){
                        String emergencyName = dataSnapshot.child(firebaseUser.getUid())
                                .child(Common.EMERGENCY_CONTACT).child(Common.EMERGENCY_NAME).getValue().toString();
                        displayEmergencyContact.setText(emergencyName);
                    }else{
                        displayEmergencyContact.setText("Please set your Emergency Contact");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            email2.setText(firebaseEmail);
            email.setText(firebaseEmail);
            //Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
        }
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final LoadingDialog loadingDialog = new LoadingDialog(SecondSplashActivity.this);
        loadingDialog.startLoadingDialog();
        if(imageUri != null){
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String uri = downloadUri.toString();

                        databaseReference = FirebaseDatabase.getInstance()
                                .getReference(Common.USER_INFORMATION).child(firebaseUser.getUid());
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put(Common.IMAGE_URL,uri);
                        databaseReference.updateChildren(hashMap);

                        loadingDialog.dismissDialog();
                    }else{
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"No image selected",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data!=null && data.getData()!=null){
            imageUri = data.getData();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getApplicationContext(),"Upload in progress...",Toast.LENGTH_SHORT).show();
            }else {
                uploadImage();
            }
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
            Toast.makeText(SecondSplashActivity.this, "\t"+personName + "\n" + personEmail,Toast.LENGTH_SHORT).show();
        }
        User user = new User();
        if(firebaseUser != null){

            user.setUserEmail(firebaseUser.getEmail());
            user.setUID(firebaseUser.getUid());
        }else{
            user.setUserEmail(null);
            user.setUID(null);
        }
    }

    private void signOut(){
        final LogoutDialog logoutDialog = new LogoutDialog(SecondSplashActivity.this);

        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        logoutDialog.startDialog();
                        Toast.makeText(SecondSplashActivity.this,"Logging out...", Toast.LENGTH_LONG).show();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateUI(null);
                                Intent intent = new Intent(SecondSplashActivity.this,SplashActivity.class);
                                startActivity(intent);
                                finish();
                                logoutDialog.dismissDialog();
                            }
                        },4000);

                    }
                });


    }
}
