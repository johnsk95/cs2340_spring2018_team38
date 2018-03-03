package com.example.team38;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ShelterDetailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail_view);

        HomelessShelter shelter = getIntent().getParcelableExtra("HomelessShelter");
        TextView infoDisplay = (TextView) findViewById(R.id.ShelterInfoBox);

        infoDisplay.setText(shelterToTextString(shelter));
    }
    String shelterToTextString(HomelessShelter shelter) {
        return "Name: \r\r" + shelter.name + "\n\nCapacity: \r\r" + shelter.capacity
                + "\n\nAllowed Tenants: \r\r" + shelter.allowedTenants + "\n\nAddress: \r\r"
                + shelter.address + "\n\nLatitude: \r\r" + shelter.latitude + "\nLongitude: \r\r"
                + shelter.longitude + "\n\nServices: \r\r" + shelter.shelterType +
                "\n\nPhone Number: \r\r" + shelter.phoneNumber;
    }
}
