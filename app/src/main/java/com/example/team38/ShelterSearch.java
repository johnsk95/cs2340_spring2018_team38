package com.example.team38;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    private TextView nameFilter;
    // --Commented out by Inspection (4/8/18 5:05 PM):private Button searchButton;
    // --Commented out by Inspection (4/8/18 5:05 PM):private RadioGroup genderRadioGroup;
    private ArrayList<HomelessShelter> shelters;
    private RadioButton menButton;
    private RadioButton womenButton;
    private RadioButton familyWithNewbornButton;
    private RadioButton childrenButton;
    private RadioButton youngAdultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection ChainedMethodCall,ChainedMethodCall
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        searchButton = (Button) findViewById(R.id.searchbutton);
//        genderRadioGroup = (RadioGroup) findViewById(R.id.genderButtonGroup);
        nameFilter = findViewById(R.id.shelterSearchNameFilterID);

        menButton = findViewById(R.id.genderMen);
        womenButton = findViewById(R.id.genderWomen);
        familyWithNewbornButton = findViewById(R.id.ageRangeFamWithNewborns);
        childrenButton = findViewById(R.id.ageRangeChildren);
        youngAdultButton = findViewById(R.id.ageRangeYoungAdults);

    }

    public void onSearchButtonClicked(@SuppressWarnings("unused") View view) {
        // NOTE; THIS CODE IS HEAVILY DUPLICATED IN SHELTERVIEWLIST.JAVA
        final DatabaseReference shelter_db;

        shelters = new ArrayList<>();
        //noinspection ChainedMethodCall
        shelter_db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com/ShelterList");
        shelter_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<HashMap<String, Object>>> typeIndicator =
                        new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
                Iterator<HashMap<String, Object>> data_iterator;
                try {
                    //noinspection ChainedMethodCall
                    data_iterator =
                        dataSnapshot.getValue(typeIndicator).iterator();
                } catch(NullPointerException e) {
                    data_iterator = new Iterator<HashMap<String, Object>>() {
                        @Override
                        public boolean hasNext() {
                            return false;
                        }

                        @Override
                        public HashMap<String, Object> next() {
                            return null;
                        }
                    };
                }
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

    private boolean namesDoNotMatch(HomelessShelter s) {
        return !s.name.toLowerCase().contains(nameFilter.getText().toString().toLowerCase());
    }
    private boolean menDoesNotMatch(HomelessShelter s) {
        return menButton.isChecked() && !s.allowed.toLowerCase().replace("women",
                "").contains("men");
    }
    private boolean womenDoesNotMatch(HomelessShelter s) {
        return womenButton.isChecked() && !s.allowed.toLowerCase().contains("women");
    }
    private boolean newbornDoesNotMatch(HomelessShelter s) {
        return familyWithNewbornButton.isChecked() &&
                !s.allowed.toLowerCase().contains("newborn");
    }
    private boolean childrenDoesNotMatch(HomelessShelter s) {
        return childrenButton.isChecked() &&
                !s.allowed.toLowerCase().contains("children");
    }
    private boolean youngAdultDoesNotMatch(HomelessShelter s) {
        return !(youngAdultButton.isChecked() &&
                !s.allowed.toLowerCase().contains("young adult"))
    }

    private boolean includeInSearch(HomelessShelter s) {

        //noinspection ChainedMethodCall,ChainedMethodCall,ChainedMethodCall
        if (namesDoNotMatch(s)) {
            return false;
        }
        //noinspection ChainedMethodCall,ChainedMethodCall
        if (menDoesNotMatch(s)) {
            return false;
        }
        //noinspection ChainedMethodCall
        if (womenDoesNotMatch(s)) {
            return false;
        }

        //noinspection ChainedMethodCall
        if (newbornDoesNotMatch(s)) {
            return false;
        }
        //noinspection ChainedMethodCall
        if (childrenDoesNotMatch(s)) {
            return false;
        }
        //noinspection ChainedMethodCall
        return youngAdultDoesNotMatch(s);
    }

    private void transferToFilteredList() {
        Intent intent = new Intent(this, ShelterListView.class);
        intent.putParcelableArrayListExtra("SheltersToDisplay", shelters);
        startActivity(intent);
    }
}

