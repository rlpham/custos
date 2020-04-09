package com.example.custos.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.custos.utils.Friends;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>  {
    private Context context;
    private List<Friends> friend;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Friends friends = friend.get(position);
        //holder.friendEmail.setText(friends.getFriendEmail());
        holder.date.setText("Since "+ friends.getDate());
        holder.friendName.setText(friends.getFriendName());
        if(friends.getImageURL().equals("default")){
            holder.friendImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(friends.getImageURL()).into(holder.friendImage);
        }
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
