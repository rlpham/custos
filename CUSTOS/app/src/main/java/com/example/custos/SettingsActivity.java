package com.example.custos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.custos.utils.Common;
import com.example.custos.utils.LogoutDialog;
import com.example.custos.utils.User;
import com.example.custos.utils.UserLocation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class SettingsActivity extends Fragment {
    public SettingsActivity() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.@return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsActivity newInstance() {
        SettingsActivity fragment = new SettingsActivity();
        //    Bundle args = new Bundle();
        //     args.putString(ARG_PARAM1, param1);
        //      args.putString(ARG_PARAM2, param2);
        //  fragment.setArguments(args);
        return fragment;
    }
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    CircleImageView imageView;
    TextView name,logout;
    GoogleSignInClient googleSignInClient;
    Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.settings, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            final public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    return true;
                }
                return false;
            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(),googleSignInOptions);

        imageView = view.findViewById(R.id.imageViewSetting);
        name = view.findViewById(R.id.textNameSetting);
        logout = view.findViewById(R.id.signout_button_setting);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder logoutDialog = new AlertDialog.Builder(getContext(),R.style.Chill);
                logoutDialog.setTitle("Logout?");
                logoutDialog.setMessage("Are you sure you want to logout?");
                logoutDialog.setIcon(R.drawable.ic_directions_run_yellow_24dp);
                logoutDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }

                });
                logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        signOut();
                    }
                });
                AlertDialog alertDialog2 = logoutDialog.create();

                // Set alertDialog "not focusable" so nav bar still hiding:
                alertDialog2.getWindow().
                        setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                // Set full-sreen mode (immersive sticky):
                alertDialog2.getWindow().getDecorView().setSystemUiVisibility(Common.ui_flags);

                // Show the alertDialog:
                alertDialog2.show();

                // Set dialog focusable so we can avoid touching outside:
                alertDialog2.getWindow().
                        clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION).child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getContext()).load(user.getImageURL()).into(imageView);
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
                if(dataSnapshot.child(firebaseUser.getUid()).child(Common.USER_NAME).exists()){
                    String userName = dataSnapshot.child(firebaseUser.getUid())
                            .child(Common.USER_NAME).getValue().toString();
                    name.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        LinearLayout layout = (LinearLayout) view.findViewById(R.id.settingscontainer);
        TextView Signout=view.findViewById(R.id.settingsSignOut);
        Signout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SecondSplashActivity.class);
                startActivity(intent);
            }
        });
        Button Report = view.findViewById(R.id.SettingReport);
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReportActivity.class);
                startActivity(intent);
            }
        });




        Button bug = view.findViewById(R.id.reportBug);
        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SubmitBugActivity.class);
                startActivity(intent);
            }
        });



        final DatabaseReference darkLight = FirebaseDatabase.getInstance().getReference("userSettings");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //placeholder atm
        final Switch sw = view.findViewById(R.id.darkMode);
        darkLight.orderByKey()
                .equalTo(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                      String darklightval=  dataSnapshot.child(firebaseUser.getUid()).child("darkmode").getValue().toString();
                      if(darklightval.equals("true")){
                          sw.setChecked(true);
                      }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    darkLight.orderByKey()
                            .equalTo(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        darkLight.child(firebaseUser.getUid()).child("darkmode").setValue("true");
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
                else
                {
                    darkLight.orderByKey()
                            .equalTo(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        darkLight.child(firebaseUser.getUid()).child("darkmode").setValue("false");
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }



            }
        });


        //location switch
         Switch loc = view.findViewById(R.id.locationSwitch);
        loc.setVisibility(GONE);



        //danger zone switch
        /**
         * Dale's code
         */
        final DatabaseReference dangerzoneswitch = FirebaseDatabase.getInstance().getReference("userSettings");
        final Switch danger = view.findViewById(R.id.dangerSwitch);
        danger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    dangerzoneswitch.orderByKey()
                            .equalTo(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dangerzoneswitch.child(firebaseUser.getUid()).child("dangerzone").setValue("true");
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
                else
                {
                    dangerzoneswitch.orderByKey()
                            .equalTo(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    darkLight.child(firebaseUser.getUid()).child("dangerzone").setValue("false");
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }



            }
        });

        dangerzoneswitch.orderByKey()
                .equalTo(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(firebaseUser.getUid()).child("dangerzone").exists()){
                            String dangerzoneval=  dataSnapshot.child(firebaseUser.getUid()).child("dangerzone").getValue().toString();
                            if(dangerzoneval.equals("true")){
                                danger.setChecked(true);
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




        // Inflate the layout for this fragment
        return view;
    }

    private void signOut(){

        final LogoutDialog logoutDialog = new LogoutDialog(getActivity());
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        logoutDialog.startDialog();
                        Toast.makeText(getContext(),"Logging out...", Toast.LENGTH_LONG).show();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateUI(null);
                                Intent intent = new Intent(getContext(),SplashActivity.class);
                                startActivity(intent);
                                logoutDialog.dismissDialog();
                            }
                        },4000);

                    }
                });


    }
    private void updateUI(FirebaseUser firebaseUser){
        //signOutButton.setVisibility(View.VISIBLE);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if(googleSignInAccount != null){
            String personName = googleSignInAccount.getDisplayName();
            String personGivenName = googleSignInAccount.getGivenName();
            String personFamilyName = googleSignInAccount.getFamilyName();
            String personEmail = googleSignInAccount.getEmail();
            String personId = googleSignInAccount.getId();
            Uri personPhoto = googleSignInAccount.getPhotoUrl();
            Toast.makeText(getContext(), "\t"+personName + "\n" + personEmail,Toast.LENGTH_SHORT).show();
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
}
