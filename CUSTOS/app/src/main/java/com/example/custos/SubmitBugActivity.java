package com.example.custos;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SubmitBugActivity extends AppCompatActivity {

    EditText et;
    Button submit;
    TextView cLeft;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_bug);

        submit = findViewById(R.id.shareReport);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitBug();
            }
        });

        cLeft = findViewById(R.id.charactersLeft);
        et = findViewById(R.id.textBoxBug);

     /*   et.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                checkBox();
                return true;
            }
        });
       */
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
     checkBox();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });








    }

    public void checkBox()
    {
        int total = 280;
        int userInput = et.getText().toString().length();

        total -= userInput;

        cLeft.setText(total + " Characters Left");
    }

    public void submitBug()
    {
        //TODO have it send it without sending user to gmail
    }


}
