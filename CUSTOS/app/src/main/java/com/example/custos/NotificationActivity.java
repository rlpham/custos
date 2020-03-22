package com.example.custos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationActivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  //  private static final String ARG_PARAM1 = "param1";
 //   private static final String ARG_PARAM2 = "param2";
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    public long numOfNotification;
    private View buttonview;
    private DBHandler database=new DBHandler();
    public NotificationActivity() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.@return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationActivity newInstance() {
        NotificationActivity fragment = new NotificationActivity();
    //    Bundle args = new Bundle();
  //     args.putString(ARG_PARAM1, param1);
  //      args.putString(ARG_PARAM2, param2);
      //  fragment.setArguments(args);
        return fragment;
    }
    private DatabaseReference db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseDatabase.getInstance().getReference("Users").child("rlpham18").child("notifications");


     //   if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
       // }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.notificationfragment, container, false);
        buttonview=view;
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.scrollnotification);
        String eventID="0000";
        String eventTitle= "TEST";
        numOfNotification=3;


        //TEST

        //Test end


           for(int i=(int)numOfNotification-1;i>=0;i--) {
               try {
                   JSONArray event = database.getNotifications();
                   eventID = event.getJSONObject(i).getString("id");
                   eventTitle = event.getJSONObject(i).getString("message");
                   generateButton(eventTitle, eventID, layout);

               } catch (JSONException e) {

               }
           }
        // Inflate the layout for this fragment
        return view;
    }

    public void generateButton(String title,String eventID,LinearLayout layout){
        ImageView imageView = new ImageView(layout.getContext());

//setting image resource
        imageView.setImageResource(R.drawable.line);

//setting image position
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

//adding view to layout
        layout.addView(imageView);

        //set the properties for button
        Button btnTag = new Button(layout.getContext());
        btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        // btnTag.setPaddingRelative(0,100,200,500);
        // btnTag.setLeftTopRightBottom(100, 100, 100);
        btnTag.setBackgroundColor(Color.parseColor("#1B1B1B"));
        btnTag.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        btnTag.setText(title);
        btnTag.setTextColor(Color.WHITE);

        buttonAction(btnTag,eventID);

        //add button to the layout
        layout.addView(btnTag);


    }


    //THIS is where the buttons are performing their action
    public void buttonAction(Button button, final String eventID){


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String firstChar= eventID.substring(0,1);

                switch (firstChar){
                    case "E": eventInvite(eventID);
                        break;
                    case "D" : safetyNotification(eventID);
                        break;
                    case "L" : eventNotification(eventID);
                        break;
                     default:
                         //Right now they are just going to main page
                         //going to redirect to eventInvite,eventNotification and userNotifications when the fragments are set up



                         Intent intent = new Intent(v.getContext(), MapsActivity.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         startActivity(intent);
                         getActivity().finish();
                         break;
                }


            }
        });
    }
    //TODO: when fragments are set up, get the event type of ID and show the appropriate changes
    public void eventInvite(String eventID){
        int eventLocation=findEventID(eventID);
        String message="";
        String sender="";
        try {
                JSONArray event = database.getNotifications();

                 message= event.getJSONObject(eventLocation).getString("message");
                 sender=event.getJSONObject(eventLocation).getString("sender");

        }catch (JSONException e){

        }

        LinearLayout layout = (LinearLayout) buttonview.findViewById(R.id.specificNotification);
        layout.setBackgroundColor(Color.parseColor("#232323"));

        //For senders name
        TextView name=new TextView(layout.getContext());
        name.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT+100));
        name.setBackgroundColor(Color.parseColor("#232323"));
        name.setTextColor(Color.WHITE);
        name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        name.setText(sender);
        layout.addView(name);

        //for now its getting message lets see later
        TextView messageView=new TextView(layout.getContext());
        messageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT+400));
        messageView.setBackgroundColor(Color.parseColor("#232323"));
        messageView.setTextColor(Color.WHITE);
        messageView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        messageView.setText(message);
        layout.addView(messageView);

        //ACCEPT REJECT
        LinearLayout arLayout = (LinearLayout) buttonview.findViewById(R.id.notificationAccept);
        arLayout.setBackgroundColor(Color.parseColor("#232323"));

       //ACCEPT
        Button btnTag = new Button(layout.getContext());
        btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // btnTag.setPaddingRelative(0,100,200,500);
        // btnTag.setLeftTopRightBottom(100, 100, 100);
        btnTag.setBackgroundColor(Color.parseColor("#036303"));

        btnTag.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnTag.setText("Accept");
        btnTag.setTextColor(Color.WHITE);

        buttonAction(btnTag,"X");

        //add button to the layout
        arLayout.addView(btnTag);

        LinearLayout rLayout = (LinearLayout) buttonview.findViewById(R.id.notificationReject);
        rLayout.setBackgroundColor(Color.parseColor("#232323"));

        //ACCEPT
        Button btnTag2 = new Button(layout.getContext());
        btnTag2.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // btnTag.setPaddingRelative(0,100,200,500);
        // btnTag.setLeftTopRightBottom(100, 100, 100);
        btnTag2.setBackgroundColor(Color.parseColor("#990000"));
        btnTag2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnTag2.setText("Reject");
        btnTag2.setTextColor(Color.WHITE);

        buttonAction(btnTag2,"X");

        //add button to the layout
        rLayout.addView(btnTag2);


    }
    public void eventNotification(String eventID){
        int eventLocation=findEventID(eventID);
        String message="";
        String sender="";
        try {
            JSONArray event = database.getNotifications();

            message= event.getJSONObject(eventLocation).getString("message");
            sender=event.getJSONObject(eventLocation).getString("sender");

        }catch (JSONException e){

        }

        LinearLayout layout = (LinearLayout) buttonview.findViewById(R.id.specificNotification);
        layout.setBackgroundColor(Color.parseColor("#232323"));

        //For senders name
        TextView name=new TextView(layout.getContext());
        name.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT+100));
        name.setBackgroundColor(Color.parseColor("#232323"));
        name.setTextColor(Color.WHITE);
        name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        name.setText(sender);
        layout.addView(name);

        //for now its getting message lets see later
        TextView messageView=new TextView(layout.getContext());
        messageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT+400));
        messageView.setBackgroundColor(Color.parseColor("#232323"));
        messageView.setTextColor(Color.WHITE);
        messageView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        messageView.setText("Description :- "+message);
        layout.addView(messageView);

        //ACCEPT REJECT
        LinearLayout arLayout = (LinearLayout) buttonview.findViewById(R.id.notificationAccept);
        arLayout.setBackgroundColor(Color.parseColor("#232323"));

        //ACCEPT
        Button btnTag = new Button(layout.getContext());
        btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // btnTag.setPaddingRelative(0,100,200,500);
        // btnTag.setLeftTopRightBottom(100, 100, 100);
        btnTag.setBackgroundColor(Color.parseColor("#036303"));

        btnTag.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnTag.setText("Accept");
        btnTag.setTextColor(Color.WHITE);

        buttonAction(btnTag,"X");
        //add button to the layout
        arLayout.addView(btnTag);

    }
    public void safetyNotification(String eventID){
        int eventLocation=findEventID(eventID);
        String message="";
        String sender="";
        try {
            JSONArray event = database.getNotifications();

            message= event.getJSONObject(eventLocation).getString("message");
            sender=event.getJSONObject(eventLocation).getString("sender");

        }catch (JSONException e){

        }

        LinearLayout layout = (LinearLayout) buttonview.findViewById(R.id.specificNotification);
        layout.setBackgroundColor(Color.parseColor("#232323"));

        //For senders name
        TextView name=new TextView(layout.getContext());
        name.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT+100));
        name.setBackgroundColor(Color.parseColor("#232323"));
        name.setTextColor(Color.WHITE);
        name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        name.setText(sender);
        layout.addView(name);

        //for now its getting message lets see later
        TextView messageView=new TextView(layout.getContext());
        messageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT+400));
        messageView.setBackgroundColor(Color.parseColor("#232323"));
        messageView.setTextColor(Color.WHITE);
        messageView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        messageView.setText("Description :- "+message);
        layout.addView(messageView);

        //ACCEPT REJECT
        LinearLayout arLayout = (LinearLayout) buttonview.findViewById(R.id.notificationAccept);
        arLayout.setBackgroundColor(Color.parseColor("#232323"));

        //ACCEPT
        Button btnTag = new Button(layout.getContext());
        btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // btnTag.setPaddingRelative(0,100,200,500);
        // btnTag.setLeftTopRightBottom(100, 100, 100);
        btnTag.setBackgroundColor(Color.parseColor("#036303"));

        btnTag.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnTag.setText("Safe");
        btnTag.setTextColor(Color.WHITE);

        buttonAction(btnTag,"X");

        //add button to the layout
        arLayout.addView(btnTag);

        LinearLayout rLayout = (LinearLayout) buttonview.findViewById(R.id.notificationReject);
        rLayout.setBackgroundColor(Color.parseColor("#232323"));

        //ACCEPT
        Button btnTag2 = new Button(layout.getContext());
        btnTag2.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // btnTag.setPaddingRelative(0,100,200,500);
        // btnTag.setLeftTopRightBottom(100, 100, 100);
        btnTag2.setBackgroundColor(Color.parseColor("#990000"));
        btnTag2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnTag2.setText("Alert");
        btnTag2.setTextColor(Color.WHITE);

        buttonAction(btnTag2,"X");

        //add button to the layout
        rLayout.addView(btnTag2);
    }


    public int findEventID(String eventID){
        int retVal=0;
        try {
            JSONArray event = database.getNotifications();
            for(int i=0;i<numOfNotification;i++){
                String temp= event.getJSONObject(i).getString("id");
                    if(temp.equals(eventID)){
                        return i;
                    }
            }
        }catch (JSONException e){

        }


        return retVal;
    }

}
