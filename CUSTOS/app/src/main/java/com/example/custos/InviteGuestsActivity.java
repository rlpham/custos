package com.example.custos;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InviteGuestsActivity extends AppCompatActivity {

    ListView listView;
    TextView invite_guests_back_button;

    ArrayList<String> selected = new ArrayList<String>();
    ArrayList<String> uids;

    Intent intent2;

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
        public View getView(final int position, View view, ViewGroup parent) {

            ArrayList<String> idk = intent2.getStringArrayListExtra("selected");
            view = inflater.inflate(R.layout.invite_guest_item, null);
            final CheckedTextView ctv = view.findViewById(R.id.checkedTextView);

            for(String element : idk) {
                if(element.equals(names[position])) {
                    ctv.setCheckMarkDrawable(R.drawable.checked);
                    ctv.setChecked(true);
                    selected.add(element);
                }
            }
            ctv.setTextColor(Color.parseColor("#FFFFFF"));
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
                    } else if (!ctv.isChecked()) {
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
        invite_guests_back_button = findViewById(R.id.invite_guests_back_button);

        intent2 = getIntent();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("contacts");
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String[] names = new String[(int)dataSnapshot.getChildrenCount()];
                int i = 0;
                for(DataSnapshot element : dataSnapshot.getChildren()) {
                    names[i] = element.child("name").getValue().toString();
                    i++;
                }
                InviteGuestsAdapter adapter = new InviteGuestsAdapter(getApplicationContext(), names);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });



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

        invite_guests_back_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

            }
        });
        
    }


}
