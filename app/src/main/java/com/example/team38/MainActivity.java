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

    /**
     * @param view operate on this
     */
    public void onLoginButtonClicked(@SuppressWarnings("unused") View view) {
        Log.d("WelcomeScreen", "Button Pressed");
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    /**
     * @param view operate here
     */
    public void onRegButtonClicked(@SuppressWarnings("unused") View view) {
        Log.d("WelcomeScreen", "REG button pressed");
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}

