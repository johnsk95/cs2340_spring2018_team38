package com.example.team38;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
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
import java.util.List;

/**
 * javadoc
 */
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

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //noinspection ChainedMethodCall,ChainedMethodCall
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        searchButton = (Button) findViewById(R.id.searchbutton);
//        genderRadioGroup = (RadioGroup) findViewById(R.id.genderButtonGroup);
        nameFilter = findViewById(R.id.shelterSearchNameFilterID);

        menButton = findViewById(R.id.genderMen);
        womenButton = findViewById(R.id.genderWomen);
        familyWithNewbornButton = findViewById(R.id.ageRangeFamWithNewborns);
        childrenButton = findViewById(R.id.ageRangeChildren);
        youngAdultButton = findViewById(R.id.ageRangeYoungAdults);

    }

    /**
     * @param view the view to operate on
     */
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

                // Literally it's supposed to be empty
                // https://www.firebase.com/docs/java-api/javadoc/com/firebase/
                // client/GenericTypeIndicator.html
                GenericTypeIndicator<ArrayList<HashMap<String, Object>>> typeIndicator =
                        new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
                Iterator<HashMap<String, Object>> data_iterator;

                List<HashMap<String, Object>> data_iterator_map =
                        dataSnapshot.getValue(typeIndicator);
                if (data_iterator_map != null) {
                    data_iterator = data_iterator_map.iterator();
                } else {
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

    // The naming convention is a bit confusing here: in general, each method returns
    // should fail because the shelter does not satisfy 'x' and we're specifically looking for 'x'

    // so for example, failOnWomen would fail if the "allowed" field doesn't contain women, and the
    // box indicating that women are being looked for is checked.
    private static boolean failOnNames(HomelessShelter s, String nameToContain) {
        return !s.getName().toLowerCase().contains(nameToContain.toLowerCase());
    }
    // true if the shelter should not be included in the search
    // true if looking for men's shelter and the shelter is not a men's shelter
    static boolean failOnMen(HomelessShelter s, boolean lookingForMen) {
        return lookingForMen && !s.getAllowed().toLowerCase().replace("women",
                "").contains("men");
    }
    static boolean failOnWomen(HomelessShelter s, boolean lookingForWomen) {

        return lookingForWomen && !s.getAllowed().toLowerCase().contains("women");
    }
    private static boolean failOnNewborn(HomelessShelter s, boolean lookingForNewborns) {
        return lookingForNewborns &&
                !s.getAllowed().toLowerCase().contains("newborn");
    }
    private static boolean childrenDoesNotMatch(HomelessShelter s, boolean lookingForChildren) {
        return lookingForChildren &&
                !s.getAllowed().toLowerCase().contains("children");
    }
    private static boolean youngAdultDoesNotMatch(HomelessShelter s, boolean lookingForYoungAdults)
    {
        return lookingForYoungAdults &&
                !s.getAllowed().toLowerCase().contains("young adult");
    }

    private boolean includeInSearch(HomelessShelter s) {

        //noinspection ChainedMethodCall,ChainedMethodCall,ChainedMethodCall
        if (failOnNames(s, nameFilter.getText().toString())) {
            return false;
        }
        //noinspection ChainedMethodCall,ChainedMethodCall
        if (failOnMen(s, menButton.isChecked())) {
            return false;
        }
        //noinspection ChainedMethodCall
        if (failOnWomen(s, womenButton.isChecked())) {
            return false;
        }

        //noinspection ChainedMethodCall
        if (failOnNewborn(s, familyWithNewbornButton.isChecked())) {
            return false;
        }
        //noinspection ChainedMethodCall
        if (childrenDoesNotMatch(s, childrenButton.isChecked())) {
            return false;
        }
        //noinspection ChainedMethodCall
        return !youngAdultDoesNotMatch(s, youngAdultButton.isChecked());
    }

    private void transferToFilteredList() {
        Intent intent = new Intent(this, ShelterListView.class);
        intent.putParcelableArrayListExtra("SheltersToDisplay", shelters);
        startActivity(intent);
    }
}

