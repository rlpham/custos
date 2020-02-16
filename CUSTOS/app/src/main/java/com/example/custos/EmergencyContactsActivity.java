package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class EmergencyContactsActivity extends AppCompatActivity {

    LinearLayout contact_group;
    LinearLayout contact_group2;
    LinearLayout contact_group3;
    LinearLayout contact_group4;
    TextView label_no;
    TextView label_no2;
    TextView label_no3;
    TextView label_no4;
    ArrayList<LinearLayout> contact_groups;
    ArrayList<TextView> number_labels;
    int add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contacts);
        add = 0;
        contact_groups = new ArrayList<LinearLayout>();
        number_labels = new ArrayList<TextView>();
        contact_group = findViewById(R.id.contact_group);
        contact_group2 = findViewById(R.id.contact_group2);
        contact_group3 = findViewById(R.id.contact_group3);
        contact_group4 = findViewById(R.id.contact_group4);
        label_no = findViewById(R.id.label_no_1);
        label_no2 = findViewById(R.id.label_no_2);
        label_no3 = findViewById(R.id.label_no_3);
        label_no4 = findViewById(R.id.label_no_4);

        contact_groups.add(contact_group);
        contact_groups.add(contact_group2);
        contact_groups.add(contact_group3);
        contact_groups.add(contact_group4);
        number_labels.add(label_no);
        number_labels.add(label_no2);
        number_labels.add(label_no3);
        number_labels.add(label_no4);
        for(int i = 0; i < contact_groups.size(); i++) {
            LinearLayout cg;
            TextView tv;
            if(i != 0) {
                cg = contact_groups.get(i);
                tv = number_labels.get(i);
                cg.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);
            }
        }


    }

    public void onClick(View view) {
        startActivity(new Intent(this, SafetyPinActivity.class));
    }

    public void addContact(View view) {
        add++;
        if(add == 4) {
            add = 0;
        }
        System.out.println(add);
        contact_groups.get(add).setVisibility(View.VISIBLE);
        number_labels.get(add).setVisibility(View.VISIBLE);

    }
}
