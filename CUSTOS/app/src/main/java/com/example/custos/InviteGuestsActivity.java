package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class InviteGuestsActivity extends AppCompatActivity {

    ListView listView;

    String[] names = {"Ryan Pham", "Emile Heskey", "Saint Laurent", "Christian Dior",
            "James Jebbia", "Madison Beer", "Kevin Pham", "Lan Le", "Plz work", "21321", "234232",
            "dskdfjalkf", "Luca Italiano", "Ryan Nguyen", "Jeremy Blum", "Lionel Messi",
            "George Washington", "Cristiano Ronaldo", "Derek Carr", "Kyler Murray"};

    ArrayList<String> selected = new ArrayList<String>();

    class InviteGuestsAdapter extends BaseAdapter {
        String[] names;
        Context context;
        LayoutInflater inflater;
        String value;

        public InviteGuestsAdapter(Context context, String[] names) {
            this.context = context;
            this.names = names;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = inflater.inflate(R.layout.invite_guest_item, null);
            final CheckedTextView ctv = view.findViewById(R.id.checkedTextView);
            ctv.setText(names[position]);
            ctv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ctv.isChecked()) {
                        value = "un-Checked";
                        ctv.setCheckMarkDrawable(0);
                        ctv.setChecked(false);
                        if(selected.contains(ctv.getText().toString())) {
                            selected.remove(ctv.getText().toString());
                        }
                    } else {
                        value = "Checked";
                        ctv.setCheckMarkDrawable(R.drawable.checked);
                        ctv.setChecked(true);
                        if(!selected.contains(ctv.getText().toString())) {
                            selected.add(ctv.getText().toString());
                        }
                    }
                }
            });

            return view;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_guests);
        // TODO: Set up checkable text views in list, then pass them into intent and test
        // https://abhiandroid.com/ui/checkedtextview
        listView = findViewById(R.id.listView);

        InviteGuestsAdapter adapter = new InviteGuestsAdapter(getApplicationContext(), names);
        listView.setAdapter(adapter);

        findViewById(R.id.invite_guests_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateEventActivity.class);
                intent.putExtra("values", selected);
                onActivityResult(18,18, intent);
                setResult(18, intent);
                finish();
            }
        });
        
    }


}
