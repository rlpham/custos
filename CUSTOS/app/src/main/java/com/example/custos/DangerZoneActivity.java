package com.example.custos;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class DangerZoneActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danger_zone);

        /**
         * Risk Level Spinner Code
         */
        Spinner riskLevelSpinner = findViewById(R.id.risklevelspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.criticallevelarray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        riskLevelSpinner.setAdapter(adapter);

        //TODO: Add in item listener



    }
}
