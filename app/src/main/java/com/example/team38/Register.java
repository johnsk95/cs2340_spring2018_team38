package com.example.team38;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Nathaniel on 2/18/2018.
 *
 * Registration screen
 */

public class Register extends AppCompatActivity {

    private EditText nameBox;
    private EditText idBox;
    private EditText passBox;
    private Spinner accountType; //Spinner used for the user type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //sets the widget variables
        nameBox = (EditText) findViewById(R.id.regname);
        idBox = (EditText) findViewById(R.id.regID);
        passBox = (EditText) findViewById(R.id.regPassword);
        accountType = (Spinner) findViewById(R.id.AccountSpinner);

        //puts the possible user types into the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, AccountType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountType.setAdapter(adapter);
    }
    /**
    Registers a new user in the map if the user id is not already in use
     */
    public void onRegisterClicked(View view) {
        Log.d("RegScreen", "Register Button Pressed");
        //registers as a new user if the id is not already in use, shows an error message if the id is in use
        if (!MainActivity.getUserMap().containsKey(idBox.getText().toString())) {
            //creates a new user
            User user = new User(nameBox.getText().toString(), idBox.getText().toString(), passBox.getText().toString(),(AccountType) accountType.getSelectedItem());
            //log of new user's info
            Log.d("newUser", "NEW USER: " + user.getName() + " " + user.getId() + " " + user.getPassword() + " " + user.getAccountType().toString());
            //adds user to userMap
            MainActivity.getUserMap().put(user.getId(), user);
            finish();
        } else {
            final Context context = getApplicationContext();
            final int duration = Toast.LENGTH_SHORT;
            final Toast t = Toast.makeText(context, "registration failed: userID is already in use", duration);
            t.show();
        }


    }
    /**
    Goes back to the main screen is cancel is pressed
     */
    public void onCancelClicked(View view) {
        finish();
    }


}
