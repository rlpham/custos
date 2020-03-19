package com.example.custos;



import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import java.util.ArrayList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ContactsActivity extends DialogFragment  {
    DBHandler db = new DBHandler();
    private String m_Text = "";
    boolean checkEdit = false;
    public SearchView searchView;

    final ArrayList<String> namesFromDB = new ArrayList<String>();
    final ArrayList<String> phoneNumbersFromDB = new ArrayList<String>();

    DatabaseReference datta;


    public ContactsActivity() {

    }

    // TODO: Rename and change types and number of parameters
    public static ContactsActivity newInstance() {
        ContactsActivity fragment = new ContactsActivity();

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    //temporary till someone can figure out how to get right user11
    datta = FirebaseDatabase.getInstance().getReference("Users").child("rlpham18").child("contacts");




    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.contactpage, container, false);

        final LinearLayout layout = (LinearLayout) view.findViewById(R.id.contactscroller);
            //LinearLayout
        final Button contactAdder = (Button) view.findViewById(R.id.button33);
            contactAdder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    addContactToLayout(contactAdder,layout);
                }


            });




            //////testing db

            final ArrayList<String> listShow = new ArrayList<String>();
            final ArrayList<String> listShow2 = new ArrayList<String>();
            datta.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    namesFromDB.clear();
                    phoneNumbersFromDB.clear();
                    for(DataSnapshot Users: dataSnapshot.getChildren())
                    {

                        int nameequal = Users.toString().indexOf("name=");
                        int comma = Users.toString().indexOf(", phone_number");
                        String contact = Users.toString().substring(nameequal+5,comma);
                        namesFromDB.add(contact);
                        listShow.add(contact);

                        System.out.println(contact);    //seeing output of names

                        int phonenumberequala = Users.toString().indexOf("phone_number=");
                        int end = Users.toString().indexOf("} }");
                        String number = Users.toString().substring(phonenumberequala+13,end);
                        phoneNumbersFromDB.add(number);
                        listShow2.add(number);
                        System.out.println(number);     //seeing output of #s
                    }
                    for(int i= 0;i < listShow.size(); i -=- 1) {


                        generateButton(listShow.get(i) + ": +" + listShow2.get(i), layout);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        //TODO somewhere below be able to update,modifly, and delete user


        return view;
    }





        public void addContactToLayout(final Button button, final LinearLayout layout)
        {







            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Enter Contact Information");
            builder.setCancelable(false);
            //builder.setMessage("test");
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.blank_page, (ViewGroup) getView(), false);

            final EditText input = (EditText) viewInflated.findViewById(R.id.input);
            final EditText input2 = (EditText) viewInflated.findViewById(R.id.input2);
            builder.setView(viewInflated);


            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    if(TextUtils.isEmpty(input.getText().toString()) || input.getText().toString().trim().length() == 0  || TextUtils.isEmpty(input2.getText().toString()) || input2.getText().toString().length() != 10) {
                        Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();
                      //  dialog.dismiss();

                        addContactToLayout(button,layout);

                    }
                    else {

                        dialog.dismiss();



                            m_Text = input.getText().toString() + ": +" + input2.getText().toString();
                           // System.out.println("test");
                            ImageView imageView = new ImageView(layout.getContext());


                            imageView.setImageResource(R.drawable.line);


                            imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                            final Button btnTag = new Button(layout.getContext());

                            btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            btnTag.setBackgroundColor(Color.parseColor("#1B1B1B"));
                            btnTag.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                            btnTag.setText(m_Text);
                            btnTag.setTextColor(Color.WHITE);

                            buttonAction(btnTag);

                            layout.addView(btnTag);





                    }




                }
            });

            builder.show();


        }






    public void generateButton(String title,LinearLayout layout){
        ImageView imageView = new ImageView(layout.getContext());


        imageView.setImageResource(R.drawable.line);


        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        final Button btnTag = new Button(layout.getContext());

        btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnTag.setBackgroundColor(Color.parseColor("#1B1B1B"));
        btnTag.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        btnTag.setText(title);
        btnTag.setTextColor(Color.WHITE);

       buttonAction(btnTag);

       layout.addView(btnTag);


    }








    public void buttonAction(final Button button)
    {

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int colon = button.getText().toString().indexOf(":");
                final String personName = button.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);

                builder.setTitle((button.getText().toString()))
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {


                        }})
                        .setNeutralButton("Delete", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {

                            deleteButton(button);
                        }})
                        .setNegativeButton("Edit", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {

                            editButton(button, personName);

                        }});
                 builder.create();
                 builder.show();

            }
        });

    }


    public void deleteButton(final Button button)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure?");
        builder.setCancelable(false);


        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                button.setVisibility(View.GONE);


            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        builder.show();
    }




    public void editButton(final Button button, final String name)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(name);
        builder.setCancelable(false);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.blank_page, (ViewGroup) getView(), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        final EditText input2 = (EditText) viewInflated.findViewById(R.id.input2);
        builder.setView(viewInflated);


        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if(TextUtils.isEmpty(input.getText().toString()) ||input.getText().toString().trim().length() == 0 || TextUtils.isEmpty(input2.getText().toString()) ||  input2.getText().toString().length() != 10) {
                    Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();

                   editButton(button,name);
                }



                    dialog.dismiss();
                    m_Text = input.getText().toString() + ": +" + input2.getText().toString();
                    button.setText(m_Text);


            }
        });

        builder.show();
    }
}




