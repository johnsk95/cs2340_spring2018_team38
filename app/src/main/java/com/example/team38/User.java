package com.example.team38;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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
    private String name;
    //the loginID of the user
    private String id;
    //what account type the user is
    private AccountType accountType;
    //the password for the user's account
    private String password;
    //the claimed shelter
    @Nullable
    private HomelessShelter shelter = null;
    //the claimed number of beds
    private int numSpots = 0;

    public User() {

    }

    public User(String name, String id, String password, AccountType accountType) {
        this.name = name;
        this.id = id;
        this.accountType = accountType;
        this.password = password;
    }

    public User(String name, String id, String password, String accountType,
                HomelessShelter shelter, int numSpots) {
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
        this.shelter = shelter;
        this.numSpots = numSpots;
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

    public HomelessShelter getShelter() {
        return shelter;
    }

    public int getNumSpots() {
        return numSpots;
    }

    public static void relinquishClaim() {
        if(currentUser.shelter == null) {
            return;
        }
        final DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long cap = dataSnapshot.child("ShelterList").child("" + currentUser.shelter.id).child("capacity")
                        .getValue(Long.class);
                if(cap == null) {
                    cap = (long) currentUser.numSpots;
                } else {
                    cap += currentUser.numSpots;
                }
                db.child("ShelterList").child("" + currentUser.shelter.id).child("capacity").setValue(cap);
                db.child("UserList").child(currentUser.getId()).child("shelter").setValue(null);
                db.child("UserList").child(currentUser.getId()).child("numSpots").setValue(0);
                currentUser.shelter = null;
                currentUser.numSpots = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static void makeClaim(final HomelessShelter shelter, final int numSpots) {
        Log.d("Claim", "Claim started");
        if(currentUser.shelter != null) {
            Log.d("ClaimFail", "User has already claimed a spot!");
            return;
        }
        if(shelter.capacity >= numSpots) {
            final DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                    "https://project-42226.firebaseio.com");
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("ClaimSuccess", "The claim was successful!");
                    Long cap = dataSnapshot.child("ShelterList/" + shelter.id)
                            .child("capacity").getValue(Long.class);
                    if(cap >= numSpots) {
                        db.child("ShelterList/" + shelter.id).child("capacity").setValue(
                                cap - numSpots);
                        db.child("UserList").child(currentUser.getId()).child("shelter").setValue(shelter);
                        db.child("UserList").child(currentUser.getId()).child("numSpots").setValue(numSpots);
                        currentUser.shelter = shelter;
                        currentUser.numSpots = numSpots;
                    } else {
                        Log.d("ClaimFail", "Capacity of shelter is inadequate");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
