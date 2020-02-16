package com.example.custos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    String regex = "^(.+)@(.+)$";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        /**
         * Necessary instance variables for email validation
         */
        String inputtedEmail = "";
        Button button = findViewById(R.id.login_button);
        EditText emailText = findViewById(R.id.username_input); //TODO: Should we change the name to email?

        /**Regular Expression being used to check if inputted Email is valid
         * Regular expression can be changed as needed
         */
    }

    /**
     * TODO: Complete this method based off where error message will be displayed on log in
     * @param inputtedEmail
     * Method created in order to validate emailInput
     * Note: That there is a regular expression for emails defined above
     * Regular expression can be changed if needed
     * In order to use the method just pass in the email from the inputted text from the user
     */
    public void emailValidation(String inputtedEmail) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputtedEmail);
        if(matcher.matches()) {
            //indicates a valid email
        }
        else{
            //Display an error message saying invalidEmail

        }

    }



}
