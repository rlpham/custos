package com.example.custos.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.custos.OtherUserActivity;
import com.example.custos.R;
import com.example.custos.utils.User;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>  {
    private Context context;
    private List<User> users;
    public FriendsAdapter(Context context, List<User> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_friends,parent,false);
        return new FriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);
        holder.userEmail.setText(user.getUserEmail());
//        if(user.getImageURL().equals("default")){
//            holder.userImage.setImageResource(R.mipmap.ic_launcher);
//        }else{
//            Glide.with(context).load(user.getImageURL()).into(holder.userImage);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherUserActivity.class);
                intent.putExtra("userid",user.getUID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView userEmail;
       // public ImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.txt_friends_email);
            //userImage = itemView.findViewById(R.id.imageFriendList);

        }
    }
}
