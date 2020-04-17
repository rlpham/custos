package com.example.custos.utils;

import android.view.View;

public class Common {
    public static final String USER_INFORMATION     = "User Information";
    public static final String USER_ADDRESS         = "User Address";
    public static final String USER_HOME_LAT        = "userHomeLatitude";
    public static final String USER_HOME_LNG        = "userHomeLongitude";
    public static final String HOME_LOC             = "homeLocation";
    public static final String FRIENDS              = "Friends";
    public static final String FRIEND_EMAIL         = "friendEmail";
    public static final String FRIEND_NAME          = "friendName";
    public static final String FRIEND_DATE          = "date";
    public static final String UID                  = "uid";
    public static final String FRIEND_REQUEST       =  "FriendRequests";
    public static final String NOTIFICATIONS        =  "Notifications";
    public static User loggedUser;
    public static final String USER_UID_SAVE_KEY    = "SaveUid";
    public static final String TOKENS               = "Tokens";
    public static final String IMAGE_UPLOAD         = "uploads";
    public static final String IMAGE_URL            = "imageURL";
    public static final String USER_PHONE           = "phoneNumber";
    public static final String USER_PIN             = "userPIN";
    public static final String USER_NAME            = "userName";
    public static final String USER_EMAIL           = "userEmail";
    public static final String EMERGENCY_CONTACT    = "emergencyContact";
    public static final String EMERGENCY_NAME       = "emergency_name";
    public static final String EMERGENCY_PHONE      = "emergency_phone";
    public static final String FROM_NAME            = "FromName";
    public static final String TO_NAME              = "ToName";
    public static final String TO_UID               = "ToUid";
    public static final String FROM_UID             = "FromUid";



    public static UserLocation currentUser;
    public static Event event;

    public static final String PASSWORD = "";
    public static final String EMAIL = "psucustos@gmail.com";

    public static int ui_flags =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

}
