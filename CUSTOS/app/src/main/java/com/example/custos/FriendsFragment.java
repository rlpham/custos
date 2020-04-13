package com.example.custos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.custos.adapters.FriendsAdapter;
import com.example.custos.utils.Common;
import com.example.custos.utils.Friends;
import com.example.custos.utils.LoadingDialog;
import com.example.custos.utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    private RecyclerView recylerView;
    private FriendsAdapter userAdapter;
    private List<Friends> friendsList;
    private TextView searchUser;
    private TextView searchIcon;
    private EditText searchSpace;
    private TextView addFriend,noFriends;

    private Context context;
    public static Fragment newInstance() {
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment,container,false);
        recylerView = view.findViewById(R.id.recycler_view_friends);
        searchIcon = view.findViewById(R.id.search_friends_icon);
        searchSpace = view.findViewById(R.id.search_friends);
        addFriend = view.findViewById(R.id.add_friend);
        noFriends = view.findViewById(R.id.no_friends);
        noFriends.setVisibility(View.INVISIBLE);
        searchIcon.setVisibility(View.VISIBLE);
        searchSpace.setVisibility(View.INVISIBLE);



        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsList = new ArrayList<>();
        readUsers();

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),UserListActivity.class);
                startActivity(intent);
            }
        });
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIcon.setVisibility(View.INVISIBLE);
                searchSpace.setVisibility(View.VISIBLE);
            }
        });

        searchUser = view.findViewById(R.id.search_friends);
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
        return view;
    }

    private void search(String user) {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance()
                .getReference(Common.FRIENDS)
                .child(fUser.getUid())
                .orderByChild(Common.FRIEND_NAME)
                .startAt(user)
                .endAt(user+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Friends friends = snapshot.getValue(Friends.class);

                    assert friends != null;
                    assert fUser != null;

                    if(!friends.getUID().equals(fUser.getUid())){
                        friendsList.add(friends);
                    }
                }
                userAdapter = new FriendsAdapter(getContext(), friendsList);
                recylerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void readUsers() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.FRIENDS).child(fUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(searchUser.getText().toString().equals("")){
                    friendsList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Friends friends = snapshot.getValue(Friends.class);
                        assert friends != null;
                        assert fUser != null;
                        friendsList.add(friends);

                    }
                    userAdapter = new FriendsAdapter(getContext(), friendsList);
                    recylerView.setAdapter(userAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference(Common.FRIENDS);
        friendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child(fUser.getUid()).exists()){
                    noFriends.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
