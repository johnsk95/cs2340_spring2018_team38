package com.example.team38;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;

/**
 *
 */
public class ShelterDetailView extends AppCompatActivity {

    private HomelessShelter shelter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Shelter Information");
        refreshShelterInfo();
    }

    private CharSequence shelterToTextString(HomelessShelter shelter) {
        if(shelter == null) {
            return "None";
        }
        return "Name: \r\r" + shelter.getName()
                + "\n\nAllowed Tenants: \r\r" + shelter.getAllowed() + "\n\nAddress: \r\r"
                + shelter.getAddress() + "\n\nLatitude: \r\r" + shelter.getLatitude()
                + "\nLongitude: \r\r"
                + shelter.getLongitude() + "\n\nServices: \r\r" + shelter.getServices() +
                "\n\nPhone Number: \r\r" + shelter.getPhone()
                + "\n\nCapacity: \r\r" + shelter.getCapacity();
    }

    /**
     * @param view passed in by default
     */
    public void onMapClicked(@SuppressWarnings("unused") View view) {
        Log.d("ShelterDetScreen", "Map Button Pressed");
        Intent intent = new Intent(this, MapsActivity.class);

        // Maps activity accepts lists of shelter views, so we make a list of the current shelter
        ArrayList<HomelessShelter> single_shelter_list = new ArrayList<HomelessShelter>();
        single_shelter_list.add(shelter);

        intent.putParcelableArrayListExtra("SheltersToDisplay",
                single_shelter_list);
        startActivity(intent);
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
