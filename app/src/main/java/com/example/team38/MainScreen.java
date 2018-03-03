package com.example.team38;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by johnyi on 2/13/18.
 *
 * Main screen where we will build the app
 *
 */

public class MainScreen extends AppCompatActivity {

    ListView shelterListView;
    ArrayList<HomelessShelter> homelessShelters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        shelterListView = (ListView) findViewById(R.id.shelter_listview);
        InputStream is = getResources().openRawResource(R.raw.homeless_shelter_spreadsheet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        homelessShelters = new ArrayList<HomelessShelter>();

        // TODO Add support for parsing from database not just local file
        try {
            // Read the first line just to clear out the info part of the csv file
            String row = reader.readLine();
            while ((row = reader.readLine()) != null) {
                HomelessShelter toAdd;
                try {
                    toAdd = new HomelessShelter(row);
                    // Log.d("application", "Created shelter " + toAdd);
                } catch (CouldNotParseInfoException e) {
                    Log.e("error", "Couldn't parse string", e);
                    throw new RuntimeException("Could not parse a homeless shelter string", e);
                }
                homelessShelters.add(toAdd);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error in reading csv file", e);
        }
        ArrayAdapter<HomelessShelter> arrayAdapter = new ArrayAdapter<HomelessShelter>(this,
                android.R.layout.simple_list_item_1, homelessShelters);
        shelterListView.setAdapter(arrayAdapter);

        shelterListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                HomelessShelter clicked = (HomelessShelter) adapter.getItemAtPosition(position);
                Intent intent = new Intent(v.getContext(), ShelterDetailView.class);
                intent.putExtra("HomelessShelter", clicked);
                startActivity(intent);
            }
        });
    }


    public void onLogoutButtonClicked(View view) {
        Log.d("MainScreen", "Button Pressed");
        Intent intent = new Intent(this, MainActivity.class);
        // prevents screen from going back to the mainScreen when pressing back button after logout
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
