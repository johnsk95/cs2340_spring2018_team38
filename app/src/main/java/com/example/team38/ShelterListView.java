package com.example.team38;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by johnyi on 2/13/18.
 *
 * Main screen where we will build the app
 *
 */

public class ShelterListView extends AppCompatActivity {

    private ListView shelterListView;
    private ArrayList<HomelessShelter> shelters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        shelterListView = findViewById(R.id.shelter_listview);

        // To allow reuse of this same activity for the search bar, I essentially
        // add a lambda expression as an argument
        //noinspection ChainedMethodCall
        shelters = getIntent().getParcelableArrayListExtra("SheltersToDisplay");
        if (shelters == null) {
            // NOTE: THIS CODE IS HEAVILY DUPLICATED IN SHELTER-SEARCH
            final DatabaseReference shelter_db;

            shelters = new ArrayList<>();
            //noinspection ChainedMethodCall
            shelter_db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                    "https://project-42226.firebaseio.com/ShelterList");
            shelter_db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // This is literally how it's supposed to be used
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

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshShelterListView();
    }
    private void refreshShelterListView() {
        ListAdapter arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, shelters);
        shelterListView.setAdapter(arrayAdapter);
        shelterListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Parcelable clicked = (HomelessShelter) adapter.getItemAtPosition(position);
                Intent intent = new Intent(v.getContext(), ShelterDetailView.class);
                intent.putExtra("HomelessShelter", clicked);
                startActivity(intent);
            }
        });
    }

    /**
     * @param view the button
     */
    public void onLogoutButtonClicked(@SuppressWarnings("unused") View view) {
        Log.d("ShelterListView", "Button Pressed");
        Intent intent = new Intent(this, MainActivity.class);
        // prevents screen from going back to the mainScreen when pressing back button after logout
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * @param view passed in automatically
     */
    public void onSearchViewButtonClicked(@SuppressWarnings("unused") View view) {
        Intent intent = new Intent(this, ShelterSearch.class);
        startActivity(intent);
    }

    /**
     * @param view passed in automatically
     */
    public void onMapButtonClicked(@SuppressWarnings("unused") View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putParcelableArrayListExtra("SheltersToDisplay", shelters);
        startActivity(intent);
    }

    /**
     * @param view passed in automatically
     */
    public void onUserInfoClicked(@SuppressWarnings("unused") View view) {
        Log.d("WelcomeScreen", "User Info button pressed");
        Intent intent = new Intent(this, UserView.class);
        startActivity(intent);
    }
}
