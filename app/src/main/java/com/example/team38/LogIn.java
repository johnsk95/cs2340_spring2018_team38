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

    /**
     * @param view the thing to operate on
     */
    public void onLoginClicked(@SuppressWarnings("unused") View view) {
        Log.d("LoginScreen", "Login Button Pressed");
        final Intent intent = new Intent(this, ShelterListView.class);
        final EditText nameBox = findViewById(R.id.ID);
        final EditText pwBox = findViewById(R.id.PW);
        //view username and password
        @SuppressWarnings("ChainedMethodCall") final String uid = nameBox.getText().toString();
        @SuppressWarnings("ChainedMethodCall") final String pass = pwBox.getText().toString();
        Log.d("user", uid);
        Log.d("pw", pass); //Why are we logging passwords?
        //checks if the user id exists and if the password is correct
        //if not a warning message is displayed
        @SuppressWarnings("ChainedMethodCall") final DatabaseReference db =
                FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com/UserList");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)) {
                    @SuppressWarnings("ChainedMethodCall") String pwd =
                            dataSnapshot.child(uid).child("password" +
                            "").getValue(String.class);
                    if(pass.equals(pwd)) {
                        Log.d("LoginScreen", "Correct login!");

                        // String name, String uid, String pass, String accountType,
                        // HomelessShelter shelter, Integer numSpots

                        String argName = dataSnapshot.child(uid).child("name")
                                .getValue(String.class);
                        String argAccountType = dataSnapshot.child(uid).child("accountType")
                                .getValue(String.class);
                        HomelessShelter argHomelessShelter = dataSnapshot.child(uid)
                                .child("shelter")
                                .getValue(HomelessShelter.class);
                        Integer argNumSpots = dataSnapshot.child(uid).child("numSpots")
                                .getValue(Integer.class);

                        User newCurrentUser = new User(argName, uid, pass, argAccountType,
                                argHomelessShelter, ((argNumSpots != null) ? argNumSpots : 0));
                        setCurrentUser(newCurrentUser);
                        startActivity(intent);
                    } else {
                        final Context context = getApplicationContext();
                        final int duration = Toast.LENGTH_SHORT;
                        final Toast t = Toast.makeText(context, "incorrect password",
                                duration);
                        t.show();
                    }
                } else {
                    final Context context = getApplicationContext();
                    final int duration = Toast.LENGTH_SHORT;
                    final Toast t = Toast.makeText(context, "the user id does not exist",
                            duration);
                    t.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    private static void setCurrentUser(User newCurrentUser) {
        User.currentUser = newCurrentUser;
    }
}
