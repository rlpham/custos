package com.example.custos;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.custos.adapters.FriendsAdapter;
//import com.example.custos.adapters.NotificationsAdapter;
import com.example.custos.utils.Common;
import com.example.custos.utils.Friends;
//import com.example.custos.utils.Notifications;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationActivity extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
//    private NotificationsAdapter adapter;
//    private List<Notifications> notificationsList;
    public NotificationActivity() {
        // Required empty public constructor
    }

    public static NotificationActivity newInstance() {
        NotificationActivity fragment = new NotificationActivity();
        //    Bundle args = new Bundle();
        //     args.putString(ARG_PARAM1, param1);
        //      args.putString(ARG_PARAM2, param2);
        //  fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificationfragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_notification);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        notificationsList = new ArrayList<>();
//        readUser();
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);

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


        return view;
    }

//    private void readUser() {
//        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Common.NOTIFICATIONS).child(fUser.getUid());
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    notificationsList.clear();
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                        Notifications notifications = snapshot.getValue(Notifications.class);
//                        assert notifications != null;
//                        assert fUser != null;
//                        notificationsList.add(notifications);
//
//                    adapter = new NotificationsAdapter(getContext(), notificationsList);
//                    recyclerView.setAdapter(adapter);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


//    public static class NotificationsViewHolder extends RecyclerView.ViewHolder{
//        public LinearLayout root;
//        public CircleImageView imageView;
//        public TextView friendName;
//        public TextView friendTime;
//
//        public NotificationsViewHolder(@NonNull View itemView) {
//            super(itemView);
//            root = itemView.findViewById(R.id.list_root);
//            imageView = itemView.findViewById(R.id.imageNotification);
//            friendName = itemView.findViewById(R.id.txt_friend_name_notification);
//            friendTime = itemView.findViewById(R.id.txt_friends_date_notification);
//
//        }
//        public void setTextName(String textName){
//            friendName.setText(textName);
//        }
//        public void setTextTime(String textTime){
//            friendTime.setText(textTime);
//        }
//    }
//
//    public void fetch(){
//
//    }
}
