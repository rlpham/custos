package com.example.custos;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.custos.R;
import com.example.custos.utils.Common;
import com.example.custos.utils.User;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

import com.example.custos.adapters.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserFragment extends Fragment {
    private RecyclerView recylerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private TextView searchUser;
    private TextView searchIcon;
    private EditText searchSpace;
    public static Fragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contactpage,container,false);
        recylerView = view.findViewById(R.id.recycler_view);
        searchIcon = view.findViewById(R.id.search_icon);
        searchSpace = view.findViewById(R.id.search_friend);
        searchIcon.setVisibility(View.VISIBLE);
        searchSpace.setVisibility(View.INVISIBLE);

        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        readUsers();

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIcon.setVisibility(View.INVISIBLE);
                searchSpace.setVisibility(View.VISIBLE);
            }
        });

        searchUser = view.findViewById(R.id.search_friend);
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
                userAdapter = new UserAdapter(getContext(),userList);
                recylerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void readUsers() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(searchUser.getText().toString().equals("")){
                    userList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        assert fUser != null;
                        if(!user.getUID().equals(fUser.getUid())){
                            userList.add(user);
                        }
                    }
                    userAdapter = new UserAdapter(getContext(),userList);
                    recylerView.setAdapter(userAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
