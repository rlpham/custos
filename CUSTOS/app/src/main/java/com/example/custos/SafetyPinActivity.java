package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        Button confirmButton;
        boolean same = false;
        int counter = 0;
        String pinmsg2 = "";
        TextView pinView2;
        Button redo;

    /**
     * Question From Dale: Are all these buttons coordinates??
     * @param savedInstanceState
     */
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
        confirmButton = findViewById(R.id.confirmSafetyButton);
        bt7 = findViewById(R.id.btn7);
        bt8 = findViewById(R.id.btn8);
        bt9 = findViewById(R.id.btn9);
        bt0 = findViewById(R.id.btn0);
        btpindelete = findViewById(R.id.deletesafetypin);
        pinView = findViewById(R.id.pinmessage);
        redo = findViewById(R.id.redoPin);

        initialSafetyMessage = findViewById(R.id.safety_pin_msg);
        pinView2 = findViewById(R.id.pinmessage2);

    }

    public void deleteNumber(View button)
    {
            initialSafetyMessage.setText("Enter safety pin");
        confirmButton.setVisibility(View.INVISIBLE);

            if(pinmsg.length() != 0 && same == false)
            {
                pinmsg = pinmsg.substring(0, pinmsg.length() - 1);
                pinView.setText(pinmsg);

            }

            if(pinmsg2.length() != 0 && same == true)
            {
                pinmsg2 = pinmsg2.substring(0, pinmsg2.length() - 1);
                pinView2.setText(pinmsg2);
            }

    }

    public void confirm(View view)
    {
        same = true;
        initialSafetyMessage.setText("Enter safety pin");
       pinView.setVisibility(View.INVISIBLE);
        confirmButton.setVisibility(View.INVISIBLE);



       if(pinmsg.equals(pinmsg2))
       {
           changePage(view);
       }
       if(!pinmsg.equals(pinmsg2) && pinmsg2.length() >= 1)
       {
            counter++;
            if(counter >= 3)
            {
          //      redo.setVisibility(View.VISIBLE);
            }

           pinmsg2 = "";
           pinView2.setText(pinmsg2);
           initialSafetyMessage.setText("Try again");
       }


    }

    public void doRedo(View view)
    {
        counter = 0;
        same = false;
        pinmsg = "";
        pinmsg2 = "";
        redo.setVisibility(View.INVISIBLE);
        pinView.setText(pinmsg);
        pinView2.setText(pinmsg2);
        pinView.setVisibility(View.VISIBLE);
        pinView2.setVisibility(View.VISIBLE);
    }


    public void changePage(View view)
    {

        Intent signupIntent = new Intent(view.getContext(), MapsActivity.class);
        startActivity(signupIntent);
    }



    public void buttonClick(View button)
    {
        if(pinmsg.length() == 3)
        {

            confirmButton.setVisibility(View.VISIBLE);
        }
        if(pinmsg2.length() == 3)
        {

            confirmButton.setVisibility(View.VISIBLE);
        }

            if(same == false)
            {
                switch (button.getId())
                {
                    case R.id.btn1:
                        if (pinmsg.length() == 4)
                        {
                            break;
                        }

                        pinmsg += "1";
                        pinView.setText(pinmsg);
                        break;

                    case R.id.btn2:
                        if (pinmsg.length() == 4)
                        {
                            break;
                        }
                        pinmsg += "2";
                        pinView.setText(pinmsg);
                        break;

                    case R.id.btn3:
                        if (pinmsg.length() == 4)
                        {
                            break;
                        }
                        pinmsg += "3";
                        pinView.setText(pinmsg);
                        break;

                    case R.id.btn4:
                        if (pinmsg.length() == 4)
                        {
                            break;
                        }
                        pinmsg += "4";
                        pinView.setText(pinmsg);
                        break;

                    case R.id.btn5:
                        if (pinmsg.length() == 4)
                        {
                            break;
                        }
                        pinmsg += "5";
                        pinView.setText(pinmsg);
                        break;

                    case R.id.btn6:
                        if (pinmsg.length() == 4)
                        {
                            break;
                        }
                        pinmsg += "6";
                        pinView.setText(pinmsg);
                        break;

                    case R.id.btn7:
                        if (pinmsg.length() == 4)
                        {
                            break;
                        }
                        pinmsg += "7";
                        pinView.setText(pinmsg);
                        break;

                    case R.id.btn8:
                        if (pinmsg.length() == 4)
                        {
                            break;
                        }
                        pinmsg += "8";
                        pinView.setText(pinmsg);
                        break;


                    case R.id.btn9:
                        if (pinmsg.length() == 4)
                        {
                            break;
                        }
                        pinmsg += "9";
                        pinView.setText(pinmsg);
                        break;

                    case R.id.btn0:
                        if (pinmsg.length() == 4)
                        {
                            break;
                        }
                        pinmsg += "0";
                        pinView.setText(pinmsg);
                        break;


                }
            }


        if(same == true)
        {
            initialSafetyMessage.setText("Enter safety pin");
            switch (button.getId())
            {
                case R.id.btn1:
                    if (pinmsg2.length() == 4)
                    {
                        break;
                    }

                    pinmsg2 += "1";
                    pinView2.setText(pinmsg2);
                    break;

                case R.id.btn2:
                    if (pinmsg2.length() == 4)
                    {
                        break;
                    }
                    pinmsg2 += "2";
                    pinView2.setText(pinmsg2);
                    break;

                case R.id.btn3:
                    if (pinmsg2.length() == 4)
                    {
                        break;
                    }
                    pinmsg2 += "3";
                    pinView2.setText(pinmsg2);
                    break;

                case R.id.btn4:
                    if (pinmsg2.length() == 4)
                    {
                        break;
                    }
                    pinmsg2 += "4";
                    pinView2.setText(pinmsg2);
                    break;

                case R.id.btn5:
                    if (pinmsg2.length() == 4)
                    {
                        break;
                    }
                    pinmsg2 += "5";
                    pinView2.setText(pinmsg2);
                    break;

                case R.id.btn6:
                    if (pinmsg2.length() == 4)
                    {
                        break;
                    }
                    pinmsg2 += "6";
                    pinView2.setText(pinmsg2);
                    break;

                case R.id.btn7:
                    if (pinmsg2.length() == 4)
                    {
                        break;
                    }
                    pinmsg2 += "7";
                    pinView2.setText(pinmsg2);
                    break;

                case R.id.btn8:
                    if (pinmsg2.length() == 4)
                    {
                        break;
                    }
                    pinmsg2 += "8";
                    pinView2.setText(pinmsg2);
                    break;


                case R.id.btn9:
                    if (pinmsg2.length() == 4)
                    {
                        break;
                    }
                    pinmsg2 += "9";
                    pinView2.setText(pinmsg2);
                    break;

                case R.id.btn0:
                    if (pinmsg2.length() == 4)
                    {
                        break;
                    }
                    pinmsg2 += "0";
                    pinView2.setText(pinmsg2);
                    break;


            }
        }

    }






}
