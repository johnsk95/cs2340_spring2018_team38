package com.example.team38;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShelterDetailView extends AppCompatActivity {

    private HomelessShelter shelter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail_view);

        shelter = getIntent().getParcelableExtra("HomelessShelter");
        Log.d("ShelterDetailView", "Shelter ID: " + shelter.id + " " + shelter.name);
        //HomelessShelter shelter_old = getIntent().getParcelableExtra("HomelessShelter");
        final DatabaseReference shelter_db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com/ShelterList/" + shelter.id);
        shelter_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HomelessShelter shelter_ = dataSnapshot.getValue(HomelessShelter.class);
                Log.d("ShelterDetailView", "Shelter DB ID: " + shelter_.id);
                setShelter(shelter_);
                TextView infoDisplay = (TextView) findViewById(R.id.ShelterInfoBox);
                infoDisplay.setText(shelterToTextString(shelter_));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        TextView infoDisplay = (TextView) findViewById(R.id.ShelterInfoBox);
//
//        infoDisplay.setText(shelterToTextString(shelter));
    }

    private CharSequence shelterToTextString(HomelessShelter shelter) {
        if(shelter == null) {
            return "None";
        }
        return "Name: \r\r" + shelter.name + "\n\nCapacity: \r\r" + shelter.capacity
                + "\n\nAllowed Tenants: \r\r" + shelter.allowed + "\n\nAddress: \r\r"
                + shelter.address + "\n\nLatitude: \r\r" + shelter.latitude + "\nLongitude: \r\r"
                + shelter.longitude + "\n\nServices: \r\r" + shelter.shelterType +
                "\n\nPhone Number: \r\r" + shelter.phoneNumber;
    }
    public void onMapClicked(View view) {
        Log.d("ShelterDetScreen", "Map Button Pressed");
        //TODO: brings the user to a map with the location of the selected shelter
    }
    public void onReserveClicked(View view) {
        Log.d("ShelterDetScreen", "Reserve Button Pressed");
        final EditText resNumBox = (EditText) findViewById(R.id.resNum);
        final int numSpots = Integer.parseInt(resNumBox.getText().toString());
        User.makeClaim(shelter, numSpots);
//        Intent intent = new Intent(this, UserView.class);
        Intent intent = new Intent(this, ShelterListView.class);
        startActivity(intent);
    }

    private void setShelter(HomelessShelter shelter) {
        this.shelter = shelter;
    }
}
