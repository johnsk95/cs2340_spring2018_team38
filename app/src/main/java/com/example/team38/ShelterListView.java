package com.example.team38;

import com.example.team38.utils.ShelterUtils;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by johnyi on 2/13/18.
 *
 * Main screen where we will build the app
 *
 */

public class ShelterListView extends AppCompatActivity {

    ListView shelterListView;
    ArrayList<HomelessShelter> shelters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        shelterListView = (ListView) findViewById(R.id.shelter_listview);

        // To allow reuse of this same activity for the search bar, I essentially
        // add a lambda expression as an argument
        shelters = getIntent().getParcelableArrayListExtra("SheltersToDisplay");
        if (shelters == null) {
            // NOTE: THIS CODE IS HEAVILY DUPLICATED IN SHELTER-SEARCH
            final DatabaseReference shelter_db;

            shelters = new ArrayList<HomelessShelter>();
            shelter_db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                    "https://project-42226.firebaseio.com/ShelterList");
            shelter_db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<ArrayList<HashMap<String, Object>>> typeIndicator =
                            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
                    Iterator<HashMap<String, Object>> data_iterator =
                            dataSnapshot.getValue(typeIndicator).iterator();
                    HashMap<String, Object> datum;
                    while (data_iterator.hasNext()) {
                        datum = data_iterator.next();
                        shelters.add(new HomelessShelter(datum));
                    }
                    refreshShelterListView();
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    refreshShelterListView();
                }
            });
        }
        refreshShelterListView();
    }
    public void refreshShelterListView() {
        ArrayAdapter<HomelessShelter> arrayAdapter = new ArrayAdapter<HomelessShelter>(this,
                android.R.layout.simple_list_item_1, shelters);
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
        Log.d("ShelterListView", "Button Pressed");
        Intent intent = new Intent(this, MainActivity.class);
        // prevents screen from going back to the mainScreen when pressing back button after logout
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void onSearchViewButtonClicked(View view) {
        Intent intent = new Intent(this, ShelterSearch.class);
        startActivity(intent);
    }
}
