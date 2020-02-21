package com.example.custos;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;

public class ContactsActivity extends AppCompatActivity {

    public TextView outputText;

    Context context;
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    String[] permissons = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG};


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        context = this;

        if (CheckPermission(ContactsActivity.this, permissons[0]))
        {
            outputText = (TextView) findViewById(R.id.textView1);
            fetchContacts();

        } else
            {
            // you do not have permission go request runtime permissions
            RequestPermission(ContactsActivity.this, permissons, REQUEST_RUNTIME_PERMISSION);
        }
    }





    public void fetchContacts()
    {

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0)
        {

            while (cursor.moveToNext())
            {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                boolean extraspace = false;
                boolean isFound = name.contains("scam") || name.contains("Scam") || name.contains("Spam") || name.contains("spam") || name.contains("Text");
                if(isFound == true)
                {
                    cursor.moveToNext();
                }


                else
                {

                if (hasPhoneNumber > 0)
                {

                    output.append("\n Name:" + name);

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                        int count = 1;

                    String samephoneNumber = "";
                    while (phoneCursor.moveToNext())
                    {

                        if(count >= 1)
                        {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            if(samephoneNumber.equals(phoneNumber))
                            {
                             break;
                            }
                        }

                        phoneNumber = samephoneNumber =  phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number " + count +": " + phoneNumber);
                        count -=- 1;
                        if(count >= 2)
                        {
                            extraspace = true;
                        }

                    }

                    phoneCursor.close();

                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,    null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);


                    //TODO if they have email add it, if not dont show
                    while (emailCursor.moveToNext())
                    {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        if(email.length() == 0)
                        {

                        }
                        else
                            {
                            output.append("\nEmail:" + email);
                        }
                    }

                    emailCursor.close();
                }

                if(extraspace) {
                    output.append("\n");
                }
                else
                {
                    extraspace = false;
                }
                }
            }

            outputText.setText(output);
        }
    }



    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults)
    {
        switch (permsRequestCode)
        {

            case REQUEST_RUNTIME_PERMISSION:
                {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // you have permission go ahead
                    outputText = (TextView) findViewById(R.id.textView1);
                    fetchContacts();

                } else
                    {
                    // you do not have permission show toast.
                }
                return;
            }
        }
    }

    public void RequestPermission(Activity thisActivity, String[] Permission, int Code)
    {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Permission[0])
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, Permission[0]))
            {
            }
            else
                {
                ActivityCompat.requestPermissions(thisActivity, Permission, Code);
            }
        }
    }

    public boolean CheckPermission(Context context, String Permission)
    {
        if (ContextCompat.checkSelfPermission(context, Permission) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
            {
            return false;
        }
    }
}






