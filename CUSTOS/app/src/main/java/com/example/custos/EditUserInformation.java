package com.example.custos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.custos.utils.Common;
import com.example.custos.utils.LoadingDialog;
import com.example.custos.utils.User;
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

public class EditUserInformation extends AppCompatActivity {
    private static final int IMAGE_REQUEST = 1;
    CircleImageView imageEdit;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private Uri imageUri;
    StorageReference storageReference;
    private StorageTask uploadTask;
    TextView editHomeLoc, backButton, changePicText;
    List<Address> addresses = new ArrayList<>();
    Geocoder geocoder;
    Button saveButton;
    TextInputLayout editPhoneNumber;
    TextInputLayout editPIN, editName;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_information);
        changePicText = findViewById(R.id.change_picture);
        imageEdit = findViewById(R.id.imageEdit);
        editHomeLoc = findViewById(R.id.editHomeLocation);
        saveButton = findViewById(R.id.saveUserInfo);
        editPhoneNumber = findViewById(R.id.textPhoneNum);
        editPIN = findViewById(R.id.textPinNumber);
        editName = findViewById(R.id.textUserName);
        backButton = findViewById(R.id.back_button_editUser);
        storageReference = FirebaseStorage.getInstance().getReference(Common.IMAGE_UPLOAD);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION).child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getImageURL().equals("default")) {
                    imageEdit.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(imageEdit);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if ((dataSnapshot.child(firebaseUser.getUid())
                            .child(Common.USER_ADDRESS)
                            .child(Common.HOME_LOC)
                            .exists())
                            && !(dataSnapshot.child(firebaseUser.getUid())
                            .child(Common.USER_ADDRESS)
                            .child(Common.HOME_LOC)
                            .getValue()
                            .toString().equals(" "))) {
                        String fullAddress = dataSnapshot.child(firebaseUser.getUid())
                                .child(Common.USER_ADDRESS)
                                .child(Common.HOME_LOC)
                                .getValue().toString();
                        editHomeLoc.setText(fullAddress);
                    } else if ((dataSnapshot.child(firebaseUser.getUid())
                            .child(Common.USER_ADDRESS)
                            .child(Common.HOME_LOC)
                            .exists()) && (dataSnapshot.child(firebaseUser.getUid())
                            .child(Common.USER_ADDRESS)
                            .child(Common.HOME_LOC)
                            .getValue()
                            .toString().equals(" "))) {
                        editHomeLoc.setText("Something went wrong try again later!");
                    } else {
                        editHomeLoc.setText("Home address is not set");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SecondSplashActivity.class);

                Pair[] pairs = new Pair[8];
                pairs[0] = new Pair<View, String>(imageEdit, "profile_picture");
                pairs[1] = new Pair<View, String>(backButton, "back");
                pairs[2] = new Pair<View, String>(changePicText, "change_picture");
                pairs[3] = new Pair<View, String>(editName, "full_name");
                pairs[4] = new Pair<View, String>(editPhoneNumber, "phone_number");
                pairs[5] = new Pair<View, String>(editPIN, "pin");
                pairs[6] = new Pair<View, String>(editHomeLoc, "address");
                pairs[7] = new Pair<View, String>(saveButton, "save_info");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(EditUserInformation.this, pairs);
                    startActivity(intent, activityOptions.toBundle());
                }

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                String phoneNumber = editPhoneNumber.getEditText().getText().toString();
                String userFullName = editName.getEditText().getText().toString();
                String userPIN = editPIN.getEditText().getText().toString();
                if (validatePhoneNumber()) {
                    if (!(phoneNumber.equals("") || phoneNumber.equals(" "))) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("phoneNumber", phoneNumber);
                        FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)
                                .child(fUser.getUid())
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditUserInformation.this, "Successful Saved", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EditUserInformation.this, "Failed Save", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }else{
                    return;
                }

//                databaseReference.child(fUser.getUid()).setValue(phoneNumber);
                if(validateName()){
                    if (!(userFullName.equals("") || userFullName.equals(" "))) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("userName", userFullName);
                        FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)
                                .child(fUser.getUid())
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditUserInformation.this, "Successful Saved", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EditUserInformation.this, "Failed Save", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }else{
                    return;
                }


                if(validatePIN()){
                    if (!(userPIN.equals("") || userPIN.equals(" "))) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("userPIN", userPIN);
                        FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)
                                .child(fUser.getUid())
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditUserInformation.this, "Successful Saved", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EditUserInformation.this, "Failed Save", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }else{
                    return;
                }



                final LoadingDialog loadingDialog = new LoadingDialog(EditUserInformation.this);
                loadingDialog.startLoadingDialog();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(EditUserInformation.this, SecondSplashActivity.class);
                        Pair[] pairs = new Pair[8];
                        pairs[0] = new Pair<View, String>(imageEdit, "profile_picture");
                        pairs[1] = new Pair<View, String>(backButton, "back");
                        pairs[2] = new Pair<View, String>(changePicText, "change_picture");
                        pairs[3] = new Pair<View, String>(editName, "full_name");
                        pairs[4] = new Pair<View, String>(editPhoneNumber, "phone_number");
                        pairs[5] = new Pair<View, String>(editPIN, "pin");
                        pairs[6] = new Pair<View, String>(editHomeLoc, "address");
                        pairs[7] = new Pair<View, String>(saveButton, "save_info");

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(EditUserInformation.this, pairs);
                            startActivity(intent, activityOptions.toBundle());
                            loadingDialog.dismissDialog();
                        }
                    }
                }, 2500);

            }


        });
        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
        editHomeLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserInformation.this, SetHomeLocationActivity.class);
                startActivity(intent);
            }
        });

    }

    private Boolean validateName() {
        String name = editName.getEditText().getText().toString();
        String letterOnly = "^[\\p{L} .'-]+$";
        if (name.isEmpty()) {
            editName.setError(null);
            editName.setErrorEnabled(false);
            return true;
        }else if (!name.matches(letterOnly)) {
            editName.setError("Letters only");
            return false;
        }else {
            editName.setError(null);
            editName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNumber() {
        String phoneNumber = editPhoneNumber.getEditText().getText().toString();
        String numberOnly = "^[0-9]{10}$";
        if (phoneNumber.isEmpty()) {
            editPhoneNumber.setError(null);
            editPhoneNumber.setErrorEnabled(false);
            return true;
        } else if (phoneNumber.length() >= 11 || phoneNumber.length() < 10) {
            editPhoneNumber.setError("Phone number must be 10 digit");
            return false;
        } else if (phoneNumber.contains(" ")) {
            editPhoneNumber.setError("White space are not allowed");
            return false;
        } else if (!phoneNumber.matches(numberOnly)) {
            editPhoneNumber.setError("Must be number only");
            return false;
        } else {
            editPhoneNumber.setError(null);
            editPhoneNumber.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePIN() {
        String PIN = editPIN.getEditText().getText().toString();
        String numberOnly = "^[0-9]{4}$";
        if (PIN.isEmpty()) {
            editPIN.setError(null);
            editPIN.setErrorEnabled(false);
            return true;
        } else if (PIN.length() >= 5 || PIN.length() < 4) {
            editPIN.setError("PIN must be 4 digit");
            return false;
        } else if (PIN.contains(" ")) {
            editPIN.setError("White space are not allowed");
            return false;
        } else if (!PIN.matches(numberOnly)) {
            editPIN.setError("Must be number only");
            return false;
        } else {
            editPIN.setError(null);
            editPIN.setErrorEnabled(false);
            return true;
        }

    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final LoadingDialog loadingDialog = new LoadingDialog(EditUserInformation.this);
        loadingDialog.startLoadingDialog();
        if (imageUri != null) {
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String uri = downloadUri.toString();

                        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION).child(firebaseUser.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put(Common.IMAGE_URL, uri);
                        databaseReference.updateChildren(hashMap);

                        loadingDialog.dismissDialog();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), "Upload in progress...", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}
