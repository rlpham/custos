package com.example.custos;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;

public class UserFragment extends Fragment {
    private RecyclerView recylerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    public static Fragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contactpage,container,false);
        recylerView = view.findViewById(R.id.recycler_view);
        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        readUsers();
        
        return view;
    }

    private void readUsers() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);

        databaseReference.addValueEventListener(new ValueEventListener() {
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
}
