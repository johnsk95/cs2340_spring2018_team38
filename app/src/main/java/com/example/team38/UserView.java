package com.example.team38;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Javadoc for UserView
 */
public class UserView extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        //HomelessShelter shelter = getIntent().getParcelableExtra("HomelessShelter");
        //reservationSuccess = getIntent().getBooleanExtra("Success", false);
        @SuppressWarnings("ChainedMethodCall") final DatabaseReference db = FirebaseDatabase
                .getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com/UserList/" + User.currentUser.getId());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView infoDisplay = findViewById(R.id.UserInfoBox);
                infoDisplay.setText(userToTextString(dataSnapshot.getValue(User.class)));
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        TextView infoDisplay = (TextView) findViewById(R.id.UserInfoBox);
//        infoDisplay.setText(userToTextString(User.currentUser));
        @SuppressWarnings("ChainedMethodCall") final DatabaseReference db =
                FirebaseDatabase.getInstance().
                getReferenceFromUrl(
                "https://project-42226.firebaseio.com/UserList/" + User.currentUser.getId());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView infoDisplay = findViewById(R.id.UserInfoBox);
                infoDisplay.setText(userToTextString(dataSnapshot.getValue(User.class)));
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
//        TextView infoDisplay = (TextView) findViewById(R.id.UserInfoBox);
//        infoDisplay.setText(userToTextString(User.currentUser));
        @SuppressWarnings("ChainedMethodCall") final DatabaseReference db =
                FirebaseDatabase.getInstance().
                getReferenceFromUrl(
                "https://project-42226.firebaseio.com/UserList/" + User.currentUser.getId());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView infoDisplay = findViewById(R.id.UserInfoBox);
                infoDisplay.setText(userToTextString(dataSnapshot.getValue(User.class)));
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }
    private CharSequence userToTextString(User user) {
        return "Name: \r\r" + user.getName()
                + "\n\nEmail: \r\r" + user.getEmail()
                + "\n\nUsername: \r\r" + user.getId()
                + "\n\nAccount Type: \r\r" + user.getAccountType()
                + "\n\nCurrently Booked Shelter: \r\r" + shelterName(user.getShelter())
                + "\n\nCurrent Reserved Spots: \r\r" + user.getNumSpots()
                /*+ "\n\nReservation: \r\r" + rsucc()*/;
    }


    private String shelterName(HomelessShelter shelter) {
        if(shelter == null) {
            return "None";
        }
//        else if(shelter.equals(HomelessShelter.NULL_SHELTER)) {
//            return "None";
//        }
        return shelter.getName();
    }

    /**
     * @param view passed in automatically
     */
    public void onLeaveShelterButtonClicked(@SuppressWarnings("unused") View view) {
        Log.d("UserViewScreen", "Leave Shelter Button Pressed");
        Toast t = Toast.makeText(getApplicationContext(),
                "Any existing reservations have been cancelled", Toast.LENGTH_SHORT);
        t.show();
        User.relinquishClaim();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                TextView infoDisplay = findViewById(R.id.UserInfoBox);
                infoDisplay.setText(userToTextString(User.currentUser));
            }
        }, Parameters.WAIT_REFRESH_MILLIS);
    }
}
