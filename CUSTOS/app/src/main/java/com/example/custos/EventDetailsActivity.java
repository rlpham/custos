package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class EventDetailsActivity extends AppCompatActivity {

    TextView event_detail_title;
    TextView event_detail_description;
    TextView event_detail_date;
    TextView event_detail_time;
    ListView event_detail_invite_list;
    Button edit_event_button;
    Button edit_event_guests_button;
    EditText event_detail_title_input;
    EditText event_detail_description_input;
    EditText event_detail_date_input;
    EditText event_detail_time_input;
    DatePickerDialog datePickerDialog;

    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        //Textviews that show event details
        event_detail_title = findViewById(R.id.event_detail_title);
        event_detail_description = findViewById(R.id.event_detail_description);
        event_detail_date = findViewById(R.id.event_detail_date);
        event_detail_time = findViewById(R.id.event_detail_time);
        event_detail_invite_list = findViewById(R.id.event_detail_invite_list);
        edit_event_button = findViewById(R.id.edit_event_button);
        edit_event_guests_button = findViewById(R.id.event_detail_invite_guests);

        //EditText that is enabled when "EDIT" is clicked
        event_detail_title_input = findViewById(R.id.event_detail_title_input);
        event_detail_description_input = findViewById(R.id.event_detail_description_input);
        event_detail_date_input = findViewById(R.id.event_detail_date_input);
        event_detail_time_input = findViewById(R.id.event_detail_time_input);

        final Intent intent = getIntent();

        String title = intent.getStringExtra("event_name");
        String description = intent.getStringExtra("event_desc");
        String date = intent.getStringExtra("event_date");
        String time = intent.getStringExtra("event_time");
        String[] invited_users = intent.getStringExtra("invited_users").split(",");

        event_detail_title.setText(title);
        event_detail_description.setText(description);
        event_detail_date.setText(date);
        event_detail_time.setText(time);

        //Initial filling in data
        event_detail_title_input.setText(event_detail_title.getText().toString());
        event_detail_description_input.setText(event_detail_description.getText().toString());
        event_detail_date_input.setText(event_detail_date.getText().toString());
        event_detail_time_input.setText(event_detail_time.getText().toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.invite_guest_list_item, R.id.aaaaaaaa, invited_users);
        event_detail_invite_list.setAdapter(adapter);


        edit_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditMode) {
                    isEditMode = true;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    edit_event_button.setText(R.string.invite_guests_done_button);

                    event_detail_title_input.setVisibility(View.VISIBLE);
                    event_detail_description_input.setVisibility(View.VISIBLE);
                    event_detail_date_input.setVisibility(View.VISIBLE);
                    event_detail_time_input.setVisibility(View.VISIBLE);

                    event_detail_title.setVisibility(View.INVISIBLE);
                    event_detail_description.setVisibility(View.INVISIBLE);
                    event_detail_date.setVisibility(View.INVISIBLE);
                    event_detail_time.setVisibility(View.INVISIBLE);

                    event_detail_title_input.setText(event_detail_title_input.getText().toString());
                    event_detail_description_input.setText(event_detail_description_input.getText().toString());
                    event_detail_date_input.setText(event_detail_date_input.getText().toString());
                    event_detail_time_input.setText(event_detail_time_input.getText().toString());

                    edit_event_guests_button.setVisibility(View.VISIBLE);

                    event_detail_title_input.requestFocus();
                } else {
                    isEditMode = false;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    edit_event_button.setText(R.string.event_edit_button_label);

                    event_detail_title_input.setVisibility(View.INVISIBLE);
                    event_detail_description_input.setVisibility(View.INVISIBLE);
                    event_detail_date_input.setVisibility(View.INVISIBLE);
                    event_detail_time_input.setVisibility(View.INVISIBLE);

                    event_detail_title.setVisibility(View.VISIBLE);
                    event_detail_description.setVisibility(View.VISIBLE);
                    event_detail_date.setVisibility(View.VISIBLE);
                    event_detail_time.setVisibility(View.VISIBLE);

                    event_detail_title.setText(event_detail_title_input.getText().toString());
                    event_detail_description.setText(event_detail_description_input.getText().toString());
                    event_detail_date.setText(event_detail_date_input.getText().toString());
                    event_detail_time.setText(event_detail_time_input.getText().toString());

                    edit_event_guests_button.setVisibility(View.INVISIBLE);


                }

            }
        });

        event_detail_date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(EventDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                event_detail_date_input.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
                                event_detail_date.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        event_detail_time_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EventDetailsActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String am_pm = "";
                        if(selectedHour > 11) {
                            selectedHour = selectedHour - 12;
                            am_pm = "PM";
                        } else {
                            am_pm = "AM";
                        }

                        if(selectedMinute < 10) {
                            event_detail_time_input.setText(selectedHour + ":0" + selectedMinute + " " + am_pm);
                            event_detail_time.setText(selectedHour + ":0" + selectedMinute + " " + am_pm);

                        } else {
                            event_detail_time_input.setText(selectedHour + ":" + selectedMinute + " " + am_pm);
                            event_detail_time.setText(selectedHour + ":0" + selectedMinute + " " + am_pm);

                        }

                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        edit_event_guests_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                Intent intent = new Intent(v.getContext(), InviteGuestsActivity.class);
                startActivityForResult(intent, 18);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 18) {
            ArrayList<String> selected = data.getStringArrayListExtra("values");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.invite_guest_list_item, R.id.aaaaaaaa, selected);
            event_detail_invite_list = findViewById(R.id.event_detail_invite_list);
            event_detail_invite_list.setAdapter(adapter);
        }
    }
}
