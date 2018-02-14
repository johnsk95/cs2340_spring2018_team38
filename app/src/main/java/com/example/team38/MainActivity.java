package com.example.team38;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * activity for welcome screen
 */
public class MainActivity extends AppCompatActivity {

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
}

