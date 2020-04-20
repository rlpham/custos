package com.example.custos;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.custos.utils.Common;
import com.example.custos.utils.User;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class InviteGuestsActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<User> users = new ArrayList<User>();
    //ArrayList<String> selectedNames = new ArrayList<String>();
    ArrayList<User> selectedUsers = new ArrayList<User>();

    Intent intent2;

    class InviteGuestsAdapter extends BaseAdapter {
        ArrayList<User> users;
        Context context;
        LayoutInflater inflater;
        String value;

        public InviteGuestsAdapter(Context context, ArrayList<User> users) {
            this.context = context;
            this.users = users;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            view = inflater.inflate(R.layout.invite_guest_item, null);
            final CheckedTextView ctv = view.findViewById(R.id.checkedTextView);


            ArrayList<String> idk  = intent2.getStringArrayListExtra("selected");

            ctv.setTextColor(Color.parseColor("#FFFFFF"));
            ctv.setText(users.get(position).getUserName());
            if(idk != null) {
                for(String element : idk) {
                    if(element.equals(ctv.getText().toString())) {
                        value = "Checked";
                        ctv.setCheckMarkDrawable(R.drawable.checked);
                        ctv.setChecked(true);
                        for(User user : users) {
                            if(user.getUserName().equals(element)) {
                                selectedUsers.add(user);
                            }
                        }
                    }
                }
            }
            ctv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ctv.isChecked()) {
                        value = "un-Checked";
                        ctv.setCheckMarkDrawable(0);
                        ctv.setChecked(false);
                        for(int i = 0; i < selectedUsers.size(); i++) {
                            if(selectedUsers.get(i).getUserName().equals(ctv.getText().toString())) {
                                selectedUsers.remove(i);
                            }
                        }
                    } else if (!ctv.isChecked()) {
                        value = "Checked";
                        ctv.setCheckMarkDrawable(R.drawable.checked);
                        ctv.setChecked(true);

                        for(int i = 0; i < users.size(); i++) {
                            if(users.get(i).getUserName().equals(ctv.getText().toString())) {
                                User user = new User();
                                user.setUserName(ctv.getText().toString());
                                user.setUID(users.get(i).getUID());
                                selectedUsers.add(user);
                            }
                        }
                    }
                }
            });
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Handler handler = new Handler();
        final View decorView = getWindow().getDecorView();

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
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    decorView.setSystemUiVisibility(uiOptions);
                                }
                            }, 2000);
                        } else {

                        }
                    }
                });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_guests);
        // TODO: Set up checkable text views in list, then pass them into intent and test
        // https://abhiandroid.com/ui/checkedtextview
        listView = findViewById(R.id.listView);
        intent2 = getIntent();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Friends").child(firebaseUser.getUid());
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot element : dataSnapshot.getChildren()) {
                    User user = new User();
                    user.setUserName(element.child("friendName").getValue().toString());
                    user.setUID(element.getKey());
                    i++;
                    users.add(user);
                }
                InviteGuestsAdapter adapter = new InviteGuestsAdapter(getApplicationContext(), users);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });



        findViewById(R.id.invite_guests_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateEventActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable)selectedUsers);
                intent.putExtra("BUNDLE", args);
                onActivityResult(18,18, intent);
                setResult(18, intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }


}
