package com.example.custos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.custos.utils.DangerZone;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class DangerZoneActivity extends AppCompatActivity {

    int highdangerzone = 2;
    int mediumdangerzone = 3;
    String userInputDangerLevel;
    String userInputDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danger_zone);

        /**
         * Risk Level Spinner Code
         */
        final Spinner riskLevelSpinner = findViewById(R.id.risklevelspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.criticallevelarray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        riskLevelSpinner.setAdapter(adapter);


        /**
         * Description Text Box Code
         */
        final EditText descriptionFillIn = findViewById(R.id.descriptionfillin);


        /**
         * Adding in action listener to submit button
         */
        Button submitdangerzonebutton = findViewById(R.id.submitdangerzonebutton);
        submitdangerzonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add in handler code here
                int requestCode = 0;
                Intent dangerIntent = new Intent(DangerZoneActivity.this, MapsActivity.class);

                userInputDangerLevel = riskLevelSpinner.getSelectedItem().toString();
                userInputDescription = descriptionFillIn.getText().toString();

                DatabaseReference dangerZonedb = FirebaseDatabase.getInstance().getReference("Danger Zone Markers");
                DangerZone createdDangerZone = new DangerZone(userInputDangerLevel,1.1,1.1,userInputDescription);


                dangerZonedb.child("123").child("risk_level").setValue(createdDangerZone.getCriticalLevel());
                dangerZonedb.child("123").child("description").setValue(createdDangerZone.getDescription());
                System.out.println(userInputDangerLevel);



                onActivityResult(2,2,dangerIntent);
                setResult(2,dangerIntent);
                finish();

//                if (userInputDangerLevel.equals("High")) {
//                    System.out.println(userInputDangerLevel);
//
//                    dangerIntent.putExtra("dangervalue",2);
//                    dangerIntent.putExtra("criticallevel",userInputDangerLevel);
//                    dangerIntent.putExtra("dangerdescription",userInputDescription);
//                    System.out.println("It worked");
//
//                    onActivityResult(2,2,dangerIntent);
//                    setResult(2,dangerIntent);
//                    finish();
//                }
//                else if (userInputDangerLevel.equals("Medium")) {
//                    System.out.println(userInputDangerLevel);
//
//                    dangerIntent.putExtra("dangervalue",3);
//                    dangerIntent.putExtra("criticallevel",userInputDangerLevel);
//                    dangerIntent.putExtra("dangerdescription",userInputDescription);
//                    System.out.println("It worked");
//
//                    onActivityResult(3,3,dangerIntent);
//                    setResult(3,dangerIntent);
//                    finish();
//                }
//                else if (userInputDangerLevel.equals("Low")) {
//
//                    System.out.println(userInputDangerLevel);
//
//                    dangerIntent.putExtra("dangervalue",4);
//                    dangerIntent.putExtra("criticallevel",userInputDangerLevel);
//                    dangerIntent.putExtra("dangerdescription",userInputDescription);
//                    System.out.println("It worked");
//
//                    onActivityResult(4,4,dangerIntent);
//                    setResult(4,dangerIntent);
//                    finish();
//                }

            }
        });
    }
}
