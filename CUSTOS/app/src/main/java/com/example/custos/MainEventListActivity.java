package com.example.custos;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainEventListActivity extends Fragment {

    DBHandler dbHandler;

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
                holder.eventLocation.setText("Philadelphia, PA");
                holder.eventDate.setText("06/18/2020");
                holder.eventTime.setText("6:30PM");
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
        View view;
        RecyclerView rv;
        LinearLayoutManager llm;
        view = inflater.inflate(R.layout.main_event, container, false);

        view.findViewById(R.id.add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateEventActivity.class);
                startActivityForResult(intent, 18);
            }
        });

        try {
            JSONArray data = dbHandler.getEventsList();
            rv = view.findViewById(R.id.recycler);
            llm = new LinearLayoutManager(this.getContext());
            RecyclerView.Adapter adapter = new EventListAdapter(data);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(llm);
            rv.setAdapter(adapter);
        } catch(JSONException e) {
            System.out.print(e);
            System.out.print(e);
        }
            return view;
    }

    public void generateEvent(JSONObject event, LinearLayout layout) throws JSONException {

    }

}

//1. Extend Fragment
//2. create newInstance method returning a fragment
//3. create and override onCreateView method.
//4. populate contact list using mock database
