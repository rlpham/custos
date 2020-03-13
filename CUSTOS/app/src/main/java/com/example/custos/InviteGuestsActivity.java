package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class InviteGuestsActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_guests);
        // TODO: Set up checkable text views in list, then pass them into intent and test
        // https://abhiandroid.com/ui/checkedtextview

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
