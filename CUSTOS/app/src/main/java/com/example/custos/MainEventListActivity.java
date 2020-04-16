package com.example.custos;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.custos.utils.Event;
import com.example.custos.utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    DBHandler dbHandler;
    DatabaseReference db;
    FirebaseUser firebaseUser;
    JSONArray data2;
    ArrayList<Event> data3;
    RecyclerView rv;
    View view;
    LinearLayoutManager llm;

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
        dbHandler = new DBHandler();
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
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
//            try {
//                holder.eventTitle.setText(data.getJSONObject(position).getString("name"));
//                holder.eventLocation.setText(data.getJSONObject(position).getString("location"));
//                holder.eventDate.setText(data.getJSONObject(position).getString("date"));
//                holder.eventTime.setText(data.getJSONObject(position).getString("time"));

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
                            args.putSerializable("BUNDLE", (Serializable)data.get(position).getInvited_users());
                            intent.putExtra("ARRAYLIST", args);
                            intent.putExtra("event_time", data.get(position).getTime());
                            intent.putExtra("location_name", data.get(position).getLocation_name());
                            startActivity(intent);
//                        } catch(JSONException e) {
//                            System.out.println(e);
//                        }

                    }
                });
                holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //TODO: display modal, confirming deleting
                        Toast toast = Toast.makeText(v.getContext(), "LONG HOLD", Toast.LENGTH_SHORT);
                        toast.show();
                        return true;
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

            }
        });

        rv = view.findViewById(R.id.recycler);
        llm = new LinearLayoutManager(this.getContext());

        try {
            listify();
        } catch(JSONException e) {
            System.out.println(e);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            System.out.println("DONE");
        }

    }

    public void listify() throws JSONException {
        JSONArray data = dbHandler.getEventsList();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("user_event").child(firebaseUser.getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data3 = new ArrayList<Event>();
                data2 = new JSONArray();
                for(DataSnapshot element : dataSnapshot.getChildren()) {
                    JSONObject obj = new JSONObject();
                    //Event event = new Event(id, name, area, date, time, description, location_name, invited_users);

                    String id = element.getKey();
                    String name = element.child("name").getValue().toString();
                    String area = element.child("area").getValue().toString();
                    String date = element.child("date").getValue().toString();
                    String time = element.child("time").getValue().toString();
                    String description = element.child("description").getValue().toString();
                    String location_name = element.child("location_name").getValue().toString();
                    ArrayList<User> invited_users = getInvitedUsers(dataSnapshot.child(id));
                    Event event = new Event(id, name, area, date, time, description, location_name, invited_users);

//                    try {
//                        obj.put("id", element.getKey());
//                        obj.put("name", element.child("name").getValue());
//                        obj.put("location", element.child("area").getValue());
//                        obj.put("date", element.child("date").getValue());
//                        obj.put("time", element.child("time").getValue());
//                        obj.put("description", element.child("description").getValue());
//                        obj.put("location_name", element.child("location_name").getValue());



//                        if(element.child("invited_users").getValue() == null) {
//                            obj.put("invited_users", "none");
//                        } else {
//                            //obj.put("invited_users",  getInvitedUsers(element.child("invited_users").getValue().toString()));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    data2.put(obj);
                    data3.add(event);
                }


                rv = view.findViewById(R.id.recycler);
                llm = new LinearLayoutManager(getContext());
                RecyclerView.Adapter adapter = new EventListAdapter(data3);
                rv.setHasFixedSize(true);
                rv.setLayoutManager(llm);
                rv.setAdapter(adapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //Creates list of names based off raw string value of datasnapshot
//    private ArrayList<User> getInvitedUsers(String data)  {
//        //I/System.out: {a={name=Madison Beer}, b={name=Blake Lively}, c={name=Alex Morgan}}
//        String invited_users = "";
//
//        if(data == "none") {
//            return "none";
//        } else {
//            for(int i = 0; i < data.length(); i++) {
//                if (i + 5 < data.length()) {
//                    if (data.substring(i, i + 5).equals("name=")) {
//                        int index = i + 5;
//                        for (int j = index; j < data.length(); j++) {
//                            if (data.charAt(j) == '}') {
//                                invited_users += (data.substring(index, j) + ',');
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//            return invited_users;
//        }
//    }

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
