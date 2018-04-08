package com.example.team38;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserView extends AppCompatActivity {

    boolean reservationSuccess = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        //HomelessShelter shelter = getIntent().getParcelableExtra("HomelessShelter");
        //reservationSuccess = getIntent().getBooleanExtra("Success", false);
        final DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com/UserList/" + User.currentUser.getId());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView infoDisplay = (TextView) findViewById(R.id.UserInfoBox);
                infoDisplay.setText(userToTextString(dataSnapshot.getValue(User.class)));
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                //TODO
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
//        TextView infoDisplay = (TextView) findViewById(R.id.UserInfoBox);
//        infoDisplay.setText(userToTextString(User.currentUser));
        final DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com/UserList/" + User.currentUser.getId());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView infoDisplay = (TextView) findViewById(R.id.UserInfoBox);
                infoDisplay.setText(userToTextString(dataSnapshot.getValue(User.class)));
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                //TODO
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
//        TextView infoDisplay = (TextView) findViewById(R.id.UserInfoBox);
//        infoDisplay.setText(userToTextString(User.currentUser));
        final DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com/UserList/" + User.currentUser.getId());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView infoDisplay = (TextView) findViewById(R.id.UserInfoBox);
                infoDisplay.setText(userToTextString(dataSnapshot.getValue(User.class)));
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                //TODO
            }
        });
    }
    CharSequence userToTextString(User user) {
        return "Name: \r\r" + user.getName()
                + "\n\nID: \r\r" + user.getId()
                + "\n\nAccount Type: \r\r" + user.getAccountType()
                + "\n\nCurrent Shelter: \r\r" + shelterName(user.getShelter())
                + "\n\nNum Spots: \r\r" + user.getNumSpots()
                /*+ "\n\nReservation: \r\r" + rsucc()*/;
    }

    private String rsucc() {
        if(reservationSuccess) {
            return "Successful";
        }
        return "Unsuccessful";
    }

    private String shelterName(HomelessShelter shelter) {
        if(shelter == null) {
            return "None";
        }
//        else if(shelter.equals(HomelessShelter.NULL_SHELTER)) {
//            return "None";
//        }
        return shelter.name;
    }

    public void onLeaveShelterButtonClicked(View view) {
        Log.d("UserViewScreen", "Leave Shelter Button Pressed");
        User.relinquishClaim();
        Intent intent = new Intent(this, ShelterListView.class);
        startActivity(intent);
    }
}
