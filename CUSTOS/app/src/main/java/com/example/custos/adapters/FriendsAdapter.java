package com.example.custos.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.custos.OtherUserActivity;
import com.example.custos.R;
import com.example.custos.utils.Common;
import com.example.custos.utils.Friends;
import com.example.custos.utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>  {
    private Context context;
    private List<Friends> friend;
    private DatabaseReference databaseReference;
    private String userName;
    private String imgURL;
    private DatabaseReference friendReference;
    public FriendsAdapter(Context context, List<Friends> friends){
        this.context = context;
        this.friend = friends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_friends,parent,false);
        return new FriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Friends friends = friend.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(friends.getUID()).child(Common.USER_NAME).exists()){
                    userName = dataSnapshot.child(friends.getUID())
                            .child(Common.USER_NAME).getValue().toString();
                    holder.friendName.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION).child(friends.getUID());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getImageURL().equals("default")){
                    holder.friendImage.setImageResource(R.mipmap.ic_launcher);
                }else{
                    imgURL = dataSnapshot.child(Common.IMAGE_URL).getValue().toString();
                    Glide.with(context).load(imgURL).into(holder.friendImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        friendReference = FirebaseDatabase.getInstance().getReference(Common.FRIENDS).child(firebaseUser.getUid());
//        friendReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
//                System.out.println(friends.getUID()+"_------------------------------------------------------------");
//                if(dataSnapshot.child(friends.getUID()).exists()){
//                    if(!dataSnapshot.child(friends.getUID()).child(Common.FRIEND_NAME).getValue().toString().equals(userName)){
//                        friendReference.child(friends.getUID()).child(Common.FRIEND_NAME).setValue(userName);
//                    }
//                }
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
//                        String image = dataSnapshot2.child(Common.IMAGE_URL).getValue().toString();
//                        if(dataSnapshot.child(friends.getUID()).exists()){
//                            if(!dataSnapshot.child(friends.getUID()).child(Common.IMAGE_URL).getValue().toString().equals(image)){
//                                friendReference.child(friends.getUID()).child(Common.IMAGE_URL).setValue(image);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        //holder.friendEmail.setText(friends.getFriendEmail());
        holder.date.setText("Since "+ friends.getDate());
//        holder.friendName.setText(friends.getFriendName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherUserActivity.class);
                intent.putExtra("userid",friends.getUID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friend.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView friendEmail;
        public ImageView friendImage;
        public TextView friendName;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //friendEmail = itemView.findViewById(R.id.txt_friends_email);
            friendImage = itemView.findViewById(R.id.imageFriendList);
            friendName = itemView.findViewById(R.id.txt_friend_name);
            date = itemView.findViewById(R.id.txt_friends_date);

        }
    }
}
