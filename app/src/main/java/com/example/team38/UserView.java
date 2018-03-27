package com.example.team38;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UserView extends AppCompatActivity {

    boolean reservationSuccess = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        //HomelessShelter shelter = getIntent().getParcelableExtra("HomelessShelter");
        reservationSuccess = getIntent().getBooleanExtra("Success", false);
        TextView infoDisplay = (TextView) findViewById(R.id.UserInfoBox);

        infoDisplay.setText(userToTextString(User.currentUser));
    }
    String userToTextString(User user) {
        return "Name: \r\r" + user.getName()
                + "\n\nID: \r\r" + user.getId()
                + "\n\nAccount Type: \r\r" + user.getAccountType()
                + "\n\nCurrent Shelter: \r\r" + user.getShelter().name
                + "\n\nNum Spots: \r\r" + user.getNumSpots()
                + "\n\nReservation: \r\r" + rsucc();
    }

    private String rsucc() {
        if(reservationSuccess) {
            return "Successful";
        }
        return "Unsuccessful";
    }

}
