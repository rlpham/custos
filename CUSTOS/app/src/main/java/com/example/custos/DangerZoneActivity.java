package com.example.custos;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class DangerZoneActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danger_zone);

        Spinner riskLevelSpinner = findViewById(R.id.risklevelspinner);



    }
}
