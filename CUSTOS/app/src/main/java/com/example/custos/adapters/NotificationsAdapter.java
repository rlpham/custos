package com.example.custos.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import com.example.custos.utils.Notifications;
import com.example.custos.utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    private Context context;
    private List<Notifications> notificationsList;
    private Map<Integer,Object> deletedItems;
    private DatabaseReference databaseReference;
    private String userName;
    private String imgURL;
    private DatabaseReference notificationsRef;
    private FirebaseUser firebaseUser;
    private Handler handler = new Handler();

    public NotificationsAdapter(Context context, List<Notifications> notificationsList2) {
        this.context = context;
        this.notificationsList = notificationsList2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_notifications, parent, false);
        return new NotificationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.itemView.setLongClickable(true);
        final Notifications notification = notificationsList.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);
        notificationsRef = FirebaseDatabase.getInstance().getReference(Common.NOTIFICATIONS).child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(notification.getUID()).child(Common.USER_NAME).exists()) {
                    userName = dataSnapshot.child(notification.getUID())
                            .child(Common.USER_NAME).getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION).child(notification.getUID());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getImageURL().equals("default")) {
                    holder.friendImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    imgURL = dataSnapshot.child(Common.IMAGE_URL).getValue().toString();
                    Glide.with(context).load(imgURL).into(holder.friendImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        notificationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(notification.getUID() + "-------------------------------");
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child(notification.getUID())
                            .child("request_type")
                            .getValue()
                            .toString().equals("receivedFriendRequest")) {
                        holder.friendName.setText(userName + " wants to add you as friend");

                    }
                    if (dataSnapshot.child(notification.getUID())
                            .child("request_type")
                            .getValue()
                            .toString().equals("acceptedFriendRequest")) {
                        holder.friendName.setText(userName + " accepted your friend request");
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        notificationsRef = FirebaseDatabase.getInstance().getReference(Common.NOTIFICATIONS).child(firebaseUser.getUid());
        notificationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(notification.getUID()).child(Common.FRIEND_NAME).getValue().toString().equals(userName)) {
                    notificationsRef.child(notification.getUID()).child(Common.FRIEND_NAME).setValue(userName);
                }
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                        String image = dataSnapshot2.child(Common.IMAGE_URL).getValue().toString();
                        if (!dataSnapshot.child(notification.getUID()).child(Common.IMAGE_URL).getValue().toString().equals(image)) {
                            notificationsRef.child(notification.getUID()).child(Common.IMAGE_URL).setValue(image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //holder.friendEmail.setText(friends.getFriendEmail());
        holder.date.setText("On " + notification.getRequest_time());
//        holder.friendName.setText(friends.getFriendName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherUserActivity.class);
                intent.putExtra("userid", notification.getUID());
                context.startActivity(intent);
            }
        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Chill);
//                alertDialog.setTitle("Delete Notification");
//                alertDialog.setMessage("Do you want to remove this notification?");
//                alertDialog.setIcon(R.drawable.ic_person_add_black_24dp);
//                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(final DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(context,NotificationActivity.class);
//                        context.startActivity(intent);
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                notificationsRef.child(notification.getUID()).removeValue();
//                            }
//                        },2000);
//                        dialogInterface.dismiss();
//                    }
//                });
//                alertDialog.show();
//                return false;
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView friendEmail;
        public ImageView friendImage;
        public TextView friendName;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //friendEmail = itemView.findViewById(R.id.txt_friends_email);
            friendName = itemView.findViewById(R.id.txt_friend_name_notification);
            date = itemView.findViewById(R.id.txt_friends_date_notification);
            friendImage = itemView.findViewById(R.id.imageNotification);

        }
    }
}
