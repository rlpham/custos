package com.example.custos;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.custos.utils.Common;
import com.example.custos.utils.Friends;
import com.example.custos.utils.User;

import java.util.ArrayList;
import java.util.List;

import com.example.custos.adapters.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView recylerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private TextView searchUser;
    private TextView searchIcon;
    private EditText searchSpace;
    private TextView backButton;
    private Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);
        recylerView = findViewById(R.id.recycler_view);
        searchIcon = findViewById(R.id.search_icon);
        searchSpace =findViewById(R.id.search_friend);
        backButton = findViewById(R.id.back_button_users);
        searchIcon.setVisibility(View.VISIBLE);
        searchSpace.setVisibility(View.INVISIBLE);

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

        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userList = new ArrayList<>();
        readUsers();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIcon.setVisibility(View.INVISIBLE);
                searchSpace.setVisibility(View.VISIBLE);
            }
        });

        searchUser = findViewById(R.id.search_friend);
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void search(String user) {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance()
                .getReference(Common.USER_INFORMATION)
                .orderByChild(Common.USER_EMAIL)
                .startAt(user)
                .endAt(user+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert fUser != null;
                    if(!user.getUID().equals(fUser.getUid())){
                        userList.add(user);
                    }
                }
                userAdapter = new UserAdapter(getApplicationContext(),userList);
                recylerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void readUsers() {
        final List<Friends> friendsList = new ArrayList<>();
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
        final DatabaseReference friendReference = FirebaseDatabase.getInstance().getReference(Common.FRIENDS);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(searchUser.getText().toString().equals("")){
                    userList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final User user = snapshot.getValue(User.class);
                        assert user != null;
                        assert fUser != null;
                        if(user.getUID()!=null){
                            if(!user.getUID().equals(fUser.getUid())){
                                userList.add(user);
                            }

                        }
                    }
                    userAdapter = new UserAdapter(getApplicationContext(),userList);
                    recylerView.setAdapter(userAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        friendReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(searchUser.getText().toString().equals("")){
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                        Friends friends = snapshot.getValue(Friends.class);
//                        assert friends!=null;
//                        if(dataSnapshot.child(fUser.getUid()).child(friends.getUID()).exists()){
//                            userList.remove(friends);
//                        }
//                    }
//                    userAdapter = new UserAdapter(getApplicationContext(),userList);
//                    recylerView.setAdapter(userAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }
}
