package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SafetyPinActivity extends AppCompatActivity {


        String pinmsg = "";
        Button bt1;
        Button bt2;
        Button bt3;
        Button bt4;
        Button bt5;
        Button bt6;
        Button bt7;
        Button bt8;
        Button bt9;
        Button bt0;
        Button btpindelete;
        TextView pinView;
        TextView initialSafetyMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_safety_pin);
         bt1 = findViewById(R.id.btn1);
        bt2 = findViewById(R.id.btn2);
        bt3 = findViewById(R.id.btn3);
        bt4 = findViewById(R.id.btn4);
        bt5 = findViewById(R.id.btn5);
        bt6 = findViewById(R.id.btn6);

        bt7 = findViewById(R.id.btn7);
        bt8 = findViewById(R.id.btn8);
        bt9 = findViewById(R.id.btn9);
        bt0 = findViewById(R.id.btn0);
        btpindelete = findViewById(R.id.deletesafetypin);
        pinView = findViewById(R.id.pinmessage);
        initialSafetyMessage = findViewById(R.id.safety_pin_msg);


    }

    public void deleteNumber(View button) {
            if(pinmsg.length() != 0)
            {
                pinmsg = pinmsg.substring(0, pinmsg.length() - 1);
                pinView.setText(pinmsg);

            }


    }

    public void buttonClick(View button) {
      switch(button.getId())
      {
          case R.id.btn1:
              if(pinmsg.length() == 4)
              {
                  break;
              }

          pinmsg += "1";
          pinView.setText(pinmsg);
              break;

          case R.id.btn2:
              if(pinmsg.length() == 4)
              {
                  break;
              }
              pinmsg += "2";
              pinView.setText(pinmsg);
              break;

          case R.id.btn3:
              if(pinmsg.length() == 4)
              {
                  break;
              }
              pinmsg += "3";
              pinView.setText(pinmsg);
              break;

          case R.id.btn4:
              if(pinmsg.length() == 4)
              {
                  break;
              }
              pinmsg += "4";
              pinView.setText(pinmsg);
              break;

          case R.id.btn5:
              if(pinmsg.length() == 4)
              {
                  break;
              }
              pinmsg += "5";
              pinView.setText(pinmsg);
              break;

          case R.id.btn6:
              if(pinmsg.length() == 4)
              {
                  break;
              }
              pinmsg += "6";
              pinView.setText(pinmsg);
              break;

          case R.id.btn7:
              if(pinmsg.length() == 4)
              {
                  break;
              }
              pinmsg += "7";
              pinView.setText(pinmsg);
              break;

          case R.id.btn8:
              if(pinmsg.length() == 4)
              {
                  break;
              }
              pinmsg += "8";
              pinView.setText(pinmsg);
              break;


          case R.id.btn9:
              if(pinmsg.length() == 4)
              {
                  break;
              }
              pinmsg += "9";
              pinView.setText(pinmsg);
              break;

          case R.id.btn0:
              if(pinmsg.length() == 4)
              {
                  break;
              }
              pinmsg += "0";
              pinView.setText(pinmsg);
              break;

      }


    }






}
