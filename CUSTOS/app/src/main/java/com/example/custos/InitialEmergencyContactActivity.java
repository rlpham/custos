package com.example.custos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.custos.utils.Common;
import com.example.custos.utils.SavingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InitialEmergencyContactActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    TextInputLayout name;
    TextInputLayout phone;
    TextView backButton;
    Button saveButton;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_emergency_contact);
        name = findViewById(R.id.emergency_name);
        phone = findViewById(R.id.emergency_phone);
        saveButton = findViewById(R.id.emergency_save);
        backButton = findViewById(R.id.back_button_emergency);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emergency_name = name.getEditText().getText().toString();
                final String emergency_phone = phone.getEditText().getText().toString();
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
                if(!validateName() || !validatePhoneNumber()){
                    return;
                }
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (!((emergency_name.equals("") || emergency_name.equals(" "))
                                && (emergency_phone.equals("") || emergency_phone.equals(" ")))) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(Common.EMERGENCY_NAME, emergency_name);
                            map.put(Common.EMERGENCY_PHONE, emergency_phone);
                            FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)
                                    .child(firebaseUser.getUid())
                                    .child(Common.EMERGENCY_CONTACT)
                                    .updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(InitialEmergencyContactActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(InitialEmergencyContactActivity.this, "Failed Save", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final SavingDialog savingDialog = new SavingDialog(InitialEmergencyContactActivity.this);
                savingDialog.startDialog();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        savingDialog.dismissDialog();
                        Intent intent = new Intent(InitialEmergencyContactActivity.this,EditUserInformation.class);
                        startActivity(intent);
                    }
                },4000);
            }
        });


    }
    private Boolean validateName() {
        String emergencyName = name.getEditText().getText().toString();
        String letterOnly = "^[\\p{L} .'-]+$";
        if (emergencyName.isEmpty()) {
            name.setError("Please add a name");
            return false;
        }else if(emergencyName.length() > 14){
            name.setError("Name must be less than 14 characters");
            return false;
        }
        else if (!emergencyName.matches(letterOnly)) {
            name.setError("Letters only");
            return false;
        }else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePhoneNumber() {
        String phoneNumber = phone.getEditText().getText().toString();
        String numberOnly = "^[0-9]{10}$";
        if (phoneNumber.isEmpty()) {
            phone.setError("Please add a phone number");
            return false;
        } else if (phoneNumber.length() >= 11 || phoneNumber.length() < 10) {
            phone.setError("Phone number must be 10 digit");
            return false;
        } else if (phoneNumber.contains(" ")) {
            phone.setError("White space are not allowed");
            return false;
        } else if (!phoneNumber.matches(numberOnly)) {
            phone.setError("Must be number only");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }



}
