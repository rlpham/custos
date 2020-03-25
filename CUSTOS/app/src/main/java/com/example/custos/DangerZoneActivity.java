package com.example.custos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.libraries.places.api.Places;

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
                System.out.println(userInputDangerLevel);

                userInputDescription = descriptionFillIn.getText().toString();

                if (userInputDangerLevel.equals("High")) {
                    requestCode = 2;
                    System.out.println(userInputDangerLevel);

                    dangerIntent.putExtra("dangervalue",2);
                    dangerIntent.putExtra("criticallevel",userInputDangerLevel);
                    dangerIntent.putExtra("dangerdescription",userInputDescription);
                    System.out.println("It worked");

                    onActivityResult(2,2,dangerIntent);
                    setResult(2,dangerIntent);
                    finish();
                }
                else if (userInputDangerLevel.equals("Medium")) {
                    requestCode = 3;
                    dangerIntent.putExtra("dangervalue",3);
                    System.out.println("It worked");

                    onActivityResult(3,3,dangerIntent);
                    setResult(3,dangerIntent);
                    finish();
                }
                else if (userInputDangerLevel.equals("Low")) {
                    requestCode = 4;
                    System.out.println(userInputDangerLevel);

                    dangerIntent.putExtra("dangervalue",4);
                    System.out.println("It worked");

                    onActivityResult(4,4,dangerIntent);
                    setResult(4,dangerIntent);
                    finish();
                }

            }
        });
    }
}
