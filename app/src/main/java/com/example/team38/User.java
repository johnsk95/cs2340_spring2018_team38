package com.example.team38;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Nathaniel on 2/18/2018.
 * Information Holder - hold the information for a user of the app
 */

public class User {
    public static User currentUser;

    //the name of the user
    private static String name;
    //the loginID of the user
    private static String id;
    //what account type the user is
    private AccountType accountType;
    //the password for the user's account
    private String password;
    //the claimed shelter
    private HomelessShelter shelter = null;
    //the claimed number of beds
    private int numSpots = 0;

    public User(String name, String id, String password, AccountType accountType) {
        this.name = name;
        this.id = id;
        this.accountType = accountType;
        this.password = password;
    }

    public User(String name, String id, String password, String accountType) {
        this.name = name;
        this.id = id;
        this.password = password;
        switch(accountType) {
            case ("Homeless User") :
                this.accountType = AccountType.HOMELESS_USER;
                break;
            case ("Admin") :
                this.accountType = AccountType.ADMIN;
                break;
            case ("Shelter Employee") :
                this.accountType = AccountType.SHELTER_EMPLOYEE;
                break;
            default:
                this.accountType = AccountType.HOMELESS_USER;
        }
    }

    @Override
    public String toString() {
        return "name: " + name + "  id: " + id + "  account type: " + accountType.toString() + "  password: " + password;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public AccountType getAccountType() {
        return  accountType;
    }

    public String getPassword() {
        return password;
    }

    public void relinquishClaim() {
        if(this.shelter == null && this.numSpots == 0) {
            return;
        }
        final DatabaseReference shelter_db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com/ShelterList/" + shelter.id);

    }
    public void makeClaim(final HomelessShelter shelter, final int numSpots) {
        if(this.shelter != null || this.numSpots != 0) {
            claimFailed(shelter, numSpots);
        }
        if(Integer.parseInt(shelter.capacity) <= numSpots) {
            final DatabaseReference shelter_db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                    "https://project-42226.firebaseio.com/ShelterList/" + shelter.id);
            shelter_db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    GenericTypeIndicator<ArrayList<HashMap<String, Object>>> typeIndicator =
//                            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
//                    Iterator<HashMap<String, Object>> data_iterator =
//                            dataSnapshot.getValue(typeIndicator).iterator();
//                    HashMap<String, Object> datum;
//                    HomelessShelter s = null;
//                    while (data_iterator.hasNext()) {
//                        datum = data_iterator.next();
//                        s = new HomelessShelter(datum);
//                        if(s.equals(shelter) && Integer.parseInt(s.capacity) >= numSpots) {
//                            break;
//                        }
//                    }
//                    if(s == null) {
//                        claimFailed();
//                    } else {
                    int cap = Integer.parseInt(dataSnapshot.child("capacity").getValue(String.class));
                    if(cap >= numSpots) {
                        shelter_db.child("capacity").setValue(
                                Integer.toString(cap - numSpots));
                    }
//                    shelter_db.child(Integer.toString(shelter.id))
//                            .child("capacity").setValue(
//                                    Integer.toString(Integer.parseInt(shelter.capacity)
//                                            - numSpots));
                    claimSuccessful(shelter, numSpots);
//                    }
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    claimFailed(shelter, numSpots);
                }
            });
        }
    }

    private void claimSuccessful(HomelessShelter shelter, int numSpots) {
        this.shelter = shelter;
        this.numSpots = numSpots;
        //TODO: more (notify user, etc)
    }

    private void claimFailed(HomelessShelter shelter, int numSpots) {
        //TODO more (notify user, etc)
    }
}
