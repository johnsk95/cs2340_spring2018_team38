package com.example.team38;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by johnyi on 2/13/18.
 *
 * ID PW screen
 */

public class LogIn extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginClicked(View view) {
        Log.d("LoginScreen", "Login Button Pressed");
        Intent intent = new Intent(this, MainScreen.class);
        final EditText nameBox = (EditText) findViewById(R.id.ID);
        final EditText pwBox = (EditText) findViewById(R.id.PW);
        //view username and password
        Log.d("user", nameBox.getText().toString());
        Log.d("pw", pwBox.getText().toString());
        //checks if the user id exists and if the password is correct, if not a warning message is displayed
        if (MainActivity.getUserMap().containsKey(nameBox.getText().toString())) {
            if (MainActivity.getUserMap().get(nameBox.getText().toString()).getPassword().equals(pwBox.getText().toString())) {
                startActivity(intent);
            } else {
                final Context context = getApplicationContext();
                final int duration = Toast.LENGTH_SHORT;
                final Toast t = Toast.makeText(context, "incorrect password", duration);
                t.show();
            }
        } else {
            final Context context = getApplicationContext();
            final int duration = Toast.LENGTH_SHORT;
            final Toast t = Toast.makeText(context, "the user id does not exist", duration);
            t.show();
        }
    }
}
