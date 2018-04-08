package com.example.team38;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ShelterSearch extends AppCompatActivity {

    TextView nameFilter;
    Button searchButton;
    RadioGroup genderRadioGroup;
    ArrayList<HomelessShelter> shelters;
    RadioButton menButton;
    RadioButton womenButton;
    RadioButton familyWithNewbornButton;
    RadioButton childrenButton;
    RadioButton youngAdultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        searchButton = (Button) findViewById(R.id.searchbutton);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderButtonGroup);
        nameFilter = (TextView) findViewById(R.id.shelterSearchNameFilterID);

        menButton = (RadioButton) findViewById(R.id.genderMen);
        womenButton = (RadioButton) findViewById(R.id.genderWomen);
        familyWithNewbornButton = (RadioButton) findViewById(R.id.ageRangeFamWithNewborns);
        childrenButton = (RadioButton) findViewById(R.id.ageRangeChildren);
        youngAdultButton = findViewById(R.id.ageRangeYoungAdults);

    }

    public void onSearchButtonClicked(View view) {
        // NOTE; THIS CODE IS HEAVILY DUPLICATED IN SHELTERVIEWLIST.JAVA
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
                    HomelessShelter s = new HomelessShelter(datum);
                    if (includeInSearch(s)) {
                        shelters.add(new HomelessShelter(datum));
                    }
                }
                transferToFilteredList();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                transferToFilteredList();
            }
        });

    }

    public boolean includeInSearch(HomelessShelter s) {

        if (!s.name.toLowerCase().contains(nameFilter.getText().toString().toLowerCase())) {
            return false;
        }
        // TODO This will of course mean that women shelters get returned along with men
        // ones. This should be fixed (duH)
        if (menButton.isChecked() && !s.allowed.toLowerCase().replace("women",
                "").contains("men")) {
            return false;
        }
        if (womenButton.isChecked() && !s.allowed.toLowerCase().contains("women")) {
            return false;
        }

        if (familyWithNewbornButton.isChecked() &&
                !s.allowed.toLowerCase().contains("newborn")) {
            return false;
        }
        if (childrenButton.isChecked() &&
                !s.allowed.toLowerCase().contains("children")) {
            return false;
        }
        if (youngAdultButton.isChecked() &&
                !s.allowed.toLowerCase().contains("young adult")) {
            return false;
        }

        return true;
    }

    public void transferToFilteredList() {
        Intent intent = new Intent(this, ShelterListView.class);
        intent.putParcelableArrayListExtra("SheltersToDisplay", shelters);
        startActivity(intent);
    }
}

