package com.example.team38;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by johnyi on 2/13/18.
 *
 * Main screen where we will build the app
 *
 */

public class MainScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
    }


    public void onLogoutButtonClicked(View view) {
        Log.d("MainScreen", "Button Pressed");
        Intent intent = new Intent(this, MainActivity.class);
        // prevents screen from going back to the mainScreen when pressing back button after logout
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
