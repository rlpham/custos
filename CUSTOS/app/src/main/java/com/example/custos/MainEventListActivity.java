package com.example.custos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.linear.qual.Linear;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainEventListActivity extends Fragment {

    DBHandler dbHandler;
    DatabaseReference db;
    FirebaseUser firebaseUser;
    JSONArray data2;
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
        JSONArray data;
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
                eventLocation = v.findViewById(R.id.event_location);
                eventDate = v.findViewById(R.id.event_date);
                eventTime = v.findViewById(R.id.event_time);
            }
        }

        EventListAdapter(JSONArray data) {
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
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            try {
                holder.eventTitle.setText(data.getJSONObject(position).getString("name"));
                holder.eventLocation.setText(data.getJSONObject(position).getString("location"));
                holder.eventDate.setText(data.getJSONObject(position).getString("date_time"));
                holder.eventTime.setText(data.getJSONObject(position).getString("date_time"));
            } catch(JSONException e) {
                System.out.println(e);
            }
        }

        @Override
        public int getItemCount() {
            return data.length();
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

        view.findViewById(R.id.add_event).setOnClickListener(new View.OnClickListener() {
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
                data2 = new JSONArray();
                for(DataSnapshot element : dataSnapshot.getChildren()) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("name", element.getKey());
                        obj.put("location", element.child("location").child("latitude").getValue() + ", " + element.child("location").child("longitude").getValue());
                        obj.put("date_time", element.child("date_time").getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    data2.put(obj);
                }
                rv = view.findViewById(R.id.recycler);
                llm = new LinearLayoutManager(getContext());
                RecyclerView.Adapter adapter = new EventListAdapter(data2);
                rv.setHasFixedSize(true);
                rv.setLayoutManager(llm);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
