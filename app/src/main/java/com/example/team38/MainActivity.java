package com.example.team38;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * activity for welcome screen
 */
public class MainActivity extends AppCompatActivity {

    //TODO: Put login info on Firebase
    //Map for registered users for M5
    private static Map<String, User> userMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoginButtonClicked(View view) {
        Log.d("WelcomeScreen", "Button Pressed");
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    public void onRegButtonClicked(View view) {
        Log.d("WelcomeScreen", "REG button pressed");
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public static Map<String, User> getUserMap() {
        return userMap;
    }
}

