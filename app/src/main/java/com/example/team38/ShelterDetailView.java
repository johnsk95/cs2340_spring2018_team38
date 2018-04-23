package com.example.team38;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

/**
 *
 */
public class ShelterDetailView extends AppCompatActivity {

    private HomelessShelter shelter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail_view);
        refreshShelterInfo();
    }

    private CharSequence shelterToTextString(HomelessShelter shelter) {
        if(shelter == null) {
            return "None";
        }
        return "Name: \r\r" + shelter.getName() + "\n\nCapacity: \r\r" + shelter.getCapacity()
                + "\n\nAllowed Tenants: \r\r" + shelter.getAllowed() + "\n\nAddress: \r\r"
                + shelter.getAddress() + "\n\nLatitude: \r\r" + shelter.getLatitude()
                + "\nLongitude: \r\r"
                + shelter.getLongitude() + "\n\nServices: \r\r" + shelter.getServices() +
                "\n\nPhone Number: \r\r" + shelter.getPhone();
    }

    /**
     * @param view passed in by default
     */
    public void onMapClicked(@SuppressWarnings("unused") View view) {
        Log.d("ShelterDetScreen", "Map Button Pressed");
    }

    /**
     * @param view passed in automatically
     */
    public void onReserveClicked(@SuppressWarnings("unused") View view) {
        Log.d("ShelterDetScreen", "Reserve Button Pressed");
        final EditText resNumBox = findViewById(R.id.resNum);
        @SuppressWarnings("ChainedMethodCall") final int numSpots = Integer.parseInt(resNumBox
                .getText().toString());

        Toast t = Toast.makeText(getApplicationContext(),
                "registration failed: userID is already in use", Toast.LENGTH_SHORT);

        if (User.makeClaim(shelter, numSpots)) {
            t.setText("Reservation successful!");
        } else {
            t.setText("Reservation failure: inadequate capacity, " +
                    "or user already has a reservation");
        }
        t.show();
        // TODO Make a toast indicating success or failure, refresh current view

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                refreshShelterInfo();
            }
        }, Parameters.WAIT_REFRESH_MILLIS);
    }

    private void refreshShelterInfo() {
        shelter = getIntent().getParcelableExtra("HomelessShelter");
        Log.d("ShelterDetailView", "Shelter ID: " + shelter.getId() + " " + shelter.getName());
        @SuppressWarnings("ChainedMethodCall") final DatabaseReference shelter_db =
                FirebaseDatabase.getInstance().getReferenceFromUrl(
                        "https://project-42226.firebaseio.com/ShelterList/" + shelter.getId());
        shelter_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HomelessShelter shelter_ = dataSnapshot.getValue(HomelessShelter.class);
                setShelter(shelter_);
                TextView infoDisplay = findViewById(R.id.ShelterInfoBox);
                infoDisplay.setText(shelterToTextString(shelter_));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setShelter(HomelessShelter shelter) {
        this.shelter = shelter;
    }
}
