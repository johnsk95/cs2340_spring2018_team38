package com.example.team38;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Nathaniel on 2/18/2018.
 * Information Holder - hold the information for a user of the app
 */

class User {
    static User currentUser;

    private String name;
    private String id;
    private AccountType accountType;
    private String password;
    private String email;

    //the claimed shelter
    @Nullable
    private HomelessShelter shelter = null;
    //the claimed number of beds
    private int numSpots = 0;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setShelter(@Nullable HomelessShelter shelter) {
        this.shelter = shelter;
    }

    public void setNumSpots(int numSpots) {
        this.numSpots = numSpots;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {

    }

    User(String name, String id, String password, String email) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "name: " + name + "  id: " + id + "  account type: " + accountType.toString()
                + "  password: " + password + " email " + email;
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

    public String getAccountTypeAsString() {
        return accountType.toString();
    }

    public String getPassword() {
        return password;
    }

    @Nullable
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
        @SuppressWarnings("ChainedMethodCall") final DatabaseReference db =
                FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                @SuppressWarnings("ChainedMethodCall") Long cap = dataSnapshot.child("ShelterList")
                        .child("" + currentUser.shelter.getId())
                        .child("capacity").getValue(Long.class);
                if(cap == null) {
                    cap = (long) currentUser.numSpots;
                } else {
                    cap += currentUser.numSpots;
                }
                //noinspection ChainedMethodCall,ChainedMethodCall,ChainedMethodCall
                db.child("ShelterList").child("" + currentUser.shelter.getId()).child("capacity")
                        .setValue(cap);
                //noinspection ChainedMethodCall,ChainedMethodCall,ChainedMethodCall
                db.child("UserList").child(currentUser.getId()).child("shelter").setValue(null);
                //noinspection ChainedMethodCall,ChainedMethodCall,ChainedMethodCall
                db.child("UserList").child(currentUser.getId()).child("numSpots").setValue(0);
                currentUser.shelter = null;
                currentUser.numSpots = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static boolean makeClaim(final HomelessShelter shelter, final int numSpots) {
        Log.d("Claim", "Claim started");
        if(currentUser.shelter != null) {
            Log.d("ClaimFail", "User has already claimed a spot!");
            return false;
        }
        if(shelter.getCapacity() >= numSpots) {
            final DatabaseReference db =
                    FirebaseDatabase.getInstance().getReferenceFromUrl(
                    "https://project-42226.firebaseio.com");
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("ClaimSuccess", "The claim was successful!");
                    Long cap =
                            dataSnapshot.child("ShelterList/" + shelter.getId())
                            .child("capacity").getValue(Long.class);
                    if((cap != null) && (cap >= numSpots)) {
                        //noinspection ChainedMethodCall,ChainedMethodCall
                        db.child("ShelterList/" + shelter.getId()).child("capacity").setValue(
                                cap - numSpots);
                        //noinspection ChainedMethodCall,ChainedMethodCall,ChainedMethodCall
                        db.child("UserList").child(currentUser.getId()).child("shelter")
                                .setValue(shelter);
                        //noinspection ChainedMethodCall,ChainedMethodCall,ChainedMethodCall
                        db.child("UserList").child(currentUser.getId()).child("numSpots")
                                .setValue(numSpots);
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
            return true;
        }
        return false;
    }

}
