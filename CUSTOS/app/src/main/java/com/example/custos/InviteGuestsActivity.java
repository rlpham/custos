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
    TextView invite_guests_back_button;

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

            //ArrayList<User> idk = intent2.getStringArrayListExtra("selectedUsers");
            Bundle args = intent2.getBundleExtra("BUNDLE");
            ArrayList<User> idk = (ArrayList<User>) args.getSerializable("BUNDLE");

            view = inflater.inflate(R.layout.invite_guest_item, null);
            final CheckedTextView ctv = view.findViewById(R.id.checkedTextView);

            if(idk == null) {
            //do nothing
            } else {
                //TODO: PERSIST CHECK MARK IF THE USER IS ALREADY INVITED
//                for(User element : idk) {
//                    if(element.getUserName().equals(users.get(position).getUserName())) {
//                        ctv.setCheckMarkDrawable(R.drawable.checked);
//                        ctv.setChecked(true);
//                        selectedUsers.add(element);
//                    }
//                }
            }

            ctv.setTextColor(Color.parseColor("#FFFFFF"));
            //ctv.setText(names[position]);
            ctv.setText(users.get(position).getUserName());
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
//                        if(selectedNames.contains(ctv.getText().toString())) {
//                            selectedNames.remove(ctv.getText().toString());
//                        }
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

                        //Selectedusers initial size is 0 therefore it skips over this. TODO

//                        for(int i = 0; i < selectedUsers.size(); i++) {
//                            if(selectedUsers.get(i).getUserName().equals(ctv.getText().toString())) {
//                                for(int j = 0; j < users.size(); j++) {
//                                    if(selectedUsers.get(i).getUserName().equals(users.get(j).getUserName())) {
//                                        User user = new User();
//                                        user.setUserName(ctv.getText().toString());
//                                        user.setUID((users.get(j).getUID()));
//                                        selectedUsers.add(user);
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                        if(!selectedNames.contains(ctv.getText().toString())) {
//                            selectedNames.add(ctv.getText().toString());
//                        }
                    }
                }
            });
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_guests);
        // TODO: Set up checkable text views in list, then pass them into intent and test
        // https://abhiandroid.com/ui/checkedtextview
        listView = findViewById(R.id.listView);
        invite_guests_back_button = findViewById(R.id.invite_guests_back_button);

        intent2 = getIntent();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Friends").child(firebaseUser.getUid());
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String[] names = new String[(int)dataSnapshot.getChildrenCount()];
//                String[] uids = new String[(int)dataSnapshot.getChildrenCount()];
                int i = 0;
                for(DataSnapshot element : dataSnapshot.getChildren()) {
                    //creates 2 parallel arrays (names, uid)
//                    names[i] = element.child("friendName").getValue().toString();
//                    uids[i] = element.getKey();
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
                //intent.putExtra("values", selectedUsers);
                onActivityResult(18,18, intent);
                setResult(18, intent);
                finish();
            }
        });

        invite_guests_back_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

            }
        });
        
    }


}
