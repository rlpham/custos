package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {

    TextView event_detail_title;
    TextView event_detail_description;
    TextView event_detail_date;
    TextView event_detail_time;
    ListView event_detail_invite_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        event_detail_title = findViewById(R.id.event_detail_title);
        event_detail_description = findViewById(R.id.event_detail_description);
        event_detail_date = findViewById(R.id.event_detail_date);
        event_detail_time = findViewById(R.id.event_detail_time);
        event_detail_invite_list = findViewById(R.id.event_detail_invite_list);

        Intent intent = getIntent();


        String title = intent.getStringExtra("event_name");
        String description = intent.getStringExtra("event_desc");
        String date = intent.getStringExtra("event_date");
        String time = intent.getStringExtra("event_time");
        String[] invited_users = intent.getStringExtra("invited_users").split(",");
        System.out.println(invited_users);
        event_detail_title.setText(title);
        event_detail_description.setText(description);
        event_detail_date.setText(date);
        event_detail_time.setText(time);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, invited_users);
        event_detail_invite_list.setAdapter(adapter);



    }
}
