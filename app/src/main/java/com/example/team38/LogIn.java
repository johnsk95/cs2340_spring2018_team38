package com.example.team38;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by johnyi on 2/13/18.
 *
 * ID PW screen
 */

public class LogIn extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginClicked(View view) {
        Log.d("LoginScreen", "Login Button Pressed");
        final Intent intent = new Intent(this, ShelterListView.class);
        final EditText nameBox = (EditText) findViewById(R.id.ID);
        final EditText pwBox = (EditText) findViewById(R.id.PW);
        //view username and password
        final String uid = nameBox.getText().toString();
        final String pass = pwBox.getText().toString();
        Log.d("user", uid);
        Log.d("pw", pass); //Why are we logging passwords?
        //checks if the user id exists and if the password is correct, if not a warning message is displayed
        final DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com/UserList");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)) {
                    if(dataSnapshot.child(uid).child("password").getValue(String.class).equals(pass)) {
                        Log.d("LoginScreen", "Correct login!");
                        setCurrentUser(dataSnapshot.child(uid).child("name").getValue(String.class),
                                uid, pass, dataSnapshot.child(uid).child("accountType")
                                        .getValue(String.class),
                                dataSnapshot.child(uid).child("shelter")
                                        .getValue(HomelessShelter.class),
                                dataSnapshot.child(uid).child("numSpots").getValue(Integer.class));
                        startActivity(intent);
                    } else {
                        final Context context = getApplicationContext();
                        final int duration = Toast.LENGTH_SHORT;
                        final Toast t = Toast.makeText(context, "incorrect password", duration);
                        t.show();
                    }
                } else {
                    final Context context = getApplicationContext();
                    final int duration = Toast.LENGTH_SHORT;
                    final Toast t = Toast.makeText(context, "the user id does not exist", duration);
                    t.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                //TODO
            }
        });
    }

    private void setCurrentUser(String name, String uid, String pass, String accountType,
                                HomelessShelter shelter, Integer numSpots) {
        Integer numSpots1 = numSpots;
        if (numSpots1 == null) {
            numSpots1 = 0;
        }
        //noinspection AssignmentToStaticFieldFromInstanceMethod
        User.currentUser = new User(name, uid, pass, accountType, shelter, numSpots1);
    }
}
