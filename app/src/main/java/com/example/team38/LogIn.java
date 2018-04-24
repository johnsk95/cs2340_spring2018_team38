package com.example.team38;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team38.mail.SendMailIntentService;
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

    public void onRecoverClicked(View view) {
        final EditText nameBox = findViewById(R.id.ID);
        final String lost_username = nameBox.getText().toString().toLowerCase();

        final DatabaseReference db =
                FirebaseDatabase.getInstance().getReferenceFromUrl(
                        "https://project-42226.firebaseio.com/UserList");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(lost_username)) {
                    User lostUser = dataSnapshot.child(lost_username).getValue(User.class);
                    Intent msgIntent = new Intent(getApplicationContext(),
                            SendMailIntentService.class);
                    msgIntent.putExtra(SendMailIntentService.PARAM_SUBJECT,
                            "Password recovery");
                    msgIntent.putExtra(SendMailIntentService.PARAM_MESSAGE,
                            "Your password is " + lostUser.getPassword());
                    msgIntent.putExtra(SendMailIntentService.PARAM_RECIPIENT,
                            "anish.moorthy@gmail.com");
                    startService(msgIntent);

                    final Toast t = Toast.makeText(getApplicationContext(),
                            "Sending recovery email: it should arrive in a minute",
                            Toast.LENGTH_SHORT);
                    t.show();

                } else {
                    final Toast t = Toast.makeText(getApplicationContext(),
                            "the user id does not exist",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("database", "Database read cancelled");
                final Toast t = Toast.makeText(getApplicationContext(),
                        "Error reading from database",
                        Toast.LENGTH_SHORT);
                t.show();
            }
        });
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
        @SuppressWarnings("ChainedMethodCall") final String uid = nameBox.getText().toString()
                .toLowerCase();
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

                        User newCurrentUser = dataSnapshot.child(uid).getValue(User.class);

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
