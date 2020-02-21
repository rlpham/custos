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

import androidx.annotation.IntDef;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class NotificationActivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  //  private static final String ARG_PARAM1 = "param1";
 //   private static final String ARG_PARAM2 = "param2";
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
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
        Bundle args = new Bundle();
  //     args.putString(ARG_PARAM1, param1);
  //      args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.notificationfragment, container, false);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.scrollnotification);

    generateButton("Kenny has invited you to his birthday!","X1sk24Ad",layout);
    generateButton("Kenny is going to pick you up in 10 mins!","X1sk24Ad",layout);
    generateButton("Kenny has accepted your invite to Custos Party!","X1sk24Ad",layout);
    generateButton("Kenny is Kenny!","X1sk24Ad",layout);
    generateButton("Well Kenny you always mess this up!","X1sk24Ad",layout);
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
        btnTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 75));
        // btnTag.setPaddingRelative(0,100,200,500);
        // btnTag.setLeftTopRightBottom(100, 100, 100);
        btnTag.setBackgroundColor(Color.parseColor("#1B1B1B"));
        btnTag.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        btnTag.setText(title);
        btnTag.setTextColor(Color.WHITE);

        defaultHome(btnTag);

        //add button to the layout
        layout.addView(btnTag);


    }

    public void defaultHome(Button button){


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    public void eventInvite(String eventID){


    }
    public void eventNotification(String eventID){


    }
    public void userNotification(String eventID){

    }

}
