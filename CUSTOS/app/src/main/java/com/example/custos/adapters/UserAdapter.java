package com.example.custos.adapters;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.custos.R;
import com.example.custos.utils.User;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>  {
    private Context context;
    private List<User> users;
    public UserAdapter(Context context, List<User> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_user,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.userEmail.setText(user.getUserEmail());
        if(user.getImageURL().equals("default")){
            holder.userImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(user.getImageURL()).into(holder.userImage);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView userEmail;
        public ImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.txt_user_email);
            userImage = itemView.findViewById(R.id.imageList);

        }
    }
}
