package com.example.custos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.custos.utils.Event;
import com.example.custos.utils.User;
import com.google.android.gms.common.util.JsonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class MainEventListActivity extends Fragment {

    DatabaseReference db;
    FirebaseUser firebaseUser;
    JSONArray data2;
    ArrayList<Event> data3;
    RecyclerView rv;
    View view;
    LinearLayoutManager llm;
    DatabaseReference databaseReference;
    private static int ui_flags =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

    public interface DataCallback {
        void callback(ArrayList<String> event_ids);
    }

    //EMPTY CONSTRUCTOR
    public MainEventListActivity() {

    }

    public static MainEventListActivity newInstance() {
        MainEventListActivity fragment = new MainEventListActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("user_event").child(firebaseUser.getUid());
    }

    class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
        ArrayList<Event> data;
        ArrayList<String> invited_users;

        class ViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            TextView eventTitle;
            TextView eventLocation;
            TextView eventDate;
            TextView eventTime;
            ImageView img;

            ViewHolder(View v) {
                super(v);
                cardView = v.findViewById(R.id.cv);
                eventTitle = v.findViewById(R.id.event_title);
                eventLocation = v.findViewById(R.id.card_event_location);
                eventDate = v.findViewById(R.id.event_date);
                eventTime = v.findViewById(R.id.event_time);
            }
        }

        EventListAdapter(ArrayList<Event> data) {
            this.data = data;
        }

        @Override
        public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_list_card, parent, false);

            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

                holder.eventTitle.setText(data.get(position).getName());
                holder.eventLocation.setText(data.get(position).getArea());
                holder.eventDate.setText(data.get(position).getDate());
                holder.eventTime.setText(data.get(position).getTime());

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("CLICKED HERE");
                        //try {
                            Intent intent = new Intent(getContext(), EventDetailsActivity.class);
                            intent.putExtra("event_id", data.get(position).getID());
                            intent.putExtra("event_name", data.get(position).getName());
                            intent.putExtra("event_desc", data.get(position).getDescription());
                            intent.putExtra("event_date", data.get(position).getDate());
                            Bundle args = new Bundle();
                            args.putSerializable("ARRAYLIST", (Serializable)data.get(position).getInvited_users());
                            intent.putExtra("BUNDLE", args);
                            intent.putExtra("event_time", data.get(position).getTime());
                            intent.putExtra("location_name", data.get(position).getLocation_name());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                        } catch(JSONException e) {
//                            System.out.println(e);
//                        }

                    }
                });
                holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Chill);
                        builder.setCancelable(true);
                        builder.setTitle("Delete Event")
                                .setIcon(R.drawable.ic_delete_black_24dp)
                                .setMessage("Are you sure you want to delete this event?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseReference.child(data.get(position).getID()).removeValue();

                                        
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        System.out.println("CANCELED");
                                    }
                                });

                        // Set alertDialog "not focusable" so nav bar still hiding:
                        AlertDialog dialog = builder.create();
                        dialog.getWindow().
                                setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                        // Set full-sreen mode (immersive sticky):
                        dialog.getWindow().getDecorView().setSystemUiVisibility(ui_flags);

                        // Show the alertDialog:
                        dialog.show();

                        // Set dialog focusable so we can avoid touching outside:
                        dialog.getWindow().
                                clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                        return false;
                    }
                });
//            } catch (JSONException e) {
//                System.out.println(e);
//            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_event, container, false);

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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        //420 DPI -> 210
        //560 DPI -> 330
        int dpiOffset = 0;

        switch(displayMetrics.densityDpi) {
            case 420:
                dpiOffset = 20;
                break;
            case 560:
                dpiOffset = 50;
                break;
            default:
                dpiOffset = 20;
                break;
        }
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height-dpiOffset));

        view.findViewById(R.id.add_event).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateEventActivity.class);
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        rv = view.findViewById(R.id.recycler);
        llm = new LinearLayoutManager(this.getContext());
        listify(new DataCallback() {

            @Override
            public void callback(final ArrayList<String> event_ids) {
                DatabaseReference db = FirebaseDatabase.getInstance()
                        .getReference("user_event")
                        .child(firebaseUser.getUid());

                final ArrayList<Event> events = new ArrayList<Event>();
                System.out.println("current event size: " + event_ids.size());

                for(final String id : event_ids) {
                    db.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            System.out.println("here");
                            if(dataSnapshot != null) {
                                if(dataSnapshot.hasChildren()) {
                                    String name = dataSnapshot.child("name").getValue().toString();
                                    String area = dataSnapshot.child("area").getValue().toString();
                                    String date = dataSnapshot.child("date").getValue().toString();
                                    String time = dataSnapshot.child("time").getValue().toString();
                                    String description = dataSnapshot.child("description").getValue().toString();
                                    String location_name = dataSnapshot.child("location_name").getValue().toString();
                                    ArrayList<User> invited_users = getInvitedUsers(dataSnapshot);
                                    events.add(new Event(id, name, area, date, time, description, location_name, invited_users));
                                    System.out.println("Hello");
                                    System.out.println("array list size: " + events.size());
                                    rv = view.findViewById(R.id.recycler);
                                    llm = new LinearLayoutManager(getContext());
                                    RecyclerView.Adapter adapter = new EventListAdapter(events);
                                    rv.setHasFixedSize(true);
                                    rv.setLayoutManager(llm);
                                    rv.setAdapter(adapter);
                                } else {
                                    rv = view.findViewById(R.id.recycler);
                                    llm = new LinearLayoutManager(getContext());
                                    RecyclerView.Adapter adapter = new EventListAdapter(new ArrayList<Event>());
                                    rv.setHasFixedSize(true);
                                    rv.setLayoutManager(llm);
                                    rv.setAdapter(adapter);
                                }
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            System.out.println("DONE");
        }

    }

    public void listify(final DataCallback dataCallback) {
        data3 = new ArrayList<Event>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("user_event").child(firebaseUser.getUid());

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> ids = new ArrayList<String>();
                for(DataSnapshot element : dataSnapshot.getChildren()) {
                   ids.add(element.getKey());
                }
                dataCallback.callback(ids);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<User> getInvitedUsers(DataSnapshot dataSnapshot) {
        ArrayList<User> invited_users = new ArrayList<User>();
        DataSnapshot data = dataSnapshot.child("invited_users");
        for(DataSnapshot element : data.getChildren()) {
            User user = new User();
            user.setUserName(element.child("name").getValue().toString());
            user.setUID(element.getKey());
            invited_users.add(user);
        }
        return invited_users;
    }

}
