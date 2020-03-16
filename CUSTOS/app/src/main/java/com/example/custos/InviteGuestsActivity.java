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

import java.util.ArrayList;

public class InviteGuestsActivity extends AppCompatActivity {


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
                    } else {
                        value = "Checked";
                        ctv.setCheckMarkDrawable(R.drawable.checked);
                        ctv.setChecked(true);
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
        ListView listView = findViewById(R.id.listView);
        String[] names = {"Ryan Pham", "Emile Heskey", "Saint Laurent", "Christian Dior",
                "James Jebbia", "Madison Beer", "Kevin Pham", "Lan Le", "Plz work", "21321", "234232",
                "dskdfjalkf", "Luca Italiano", "Ryan Nguyen", "Jeremy Blum", "Lionel Messi",
                "George Washington", "Cristiano Ronaldo", "Derek Carr", "Kyler Murray"};
        InviteGuestsAdapter adapter = new InviteGuestsAdapter(getApplicationContext(), names);
        listView.setAdapter(adapter);

        findViewById(R.id.invite_guests_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });



//        listView =findViewById(R.id.list);
//        ArrayList<String> names = new ArrayList<String>();
//        names.add("Ryan");
//        names.add("This");
//        names.add("asdgsa");
//        names.add("bar");
//        names.add("foo");
//        names.add("baz");
//        names.add("abcdefg");
//        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.invite_guests, names);
//        listView.setAdapter(adapter);
    }

    public void doneAddingGuests() {
        Intent invGuestIntent = new Intent();
        invGuestIntent.putExtra("Guests", "");
        setResult(1, invGuestIntent);
        finish();
    }

}
