package com.example.team38;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

/**
 * Created by Nathaniel on 2/18/2018.
 *
 * Registration screen
 */

public class Register extends AppCompatActivity {

    private EditText nameBox;
    private EditText idBox;
    private EditText passBox;
    private EditText emailBox;
    private Spinner accountType; //Spinner used for the user type


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        //sets the widget variables
        nameBox = findViewById(R.id.regname);
        idBox = findViewById(R.id.regID);
        passBox = findViewById(R.id.regPassword);
        emailBox = findViewById(R.id.emailInput);
        accountType = findViewById(R.id.AccountSpinner);

        //puts the possible user types into the spinner
        @SuppressWarnings("unchecked") ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, AccountType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountType.setAdapter(adapter);
    }
    /**
    Registers a new user in the map if the user id is not already in use
     * @param view operate on this view
     */
    public void onRegisterClicked(@SuppressWarnings("unused") View view) {
        Log.d("RegScreen", "Register Button Pressed");
        //registers as a new user if the id is not already in use,
        //shows an error message if the id is in use
        final String uid = idBox.getText().toString().toLowerCase();

        // Ensure that the userid is alphanumeric, since it will be used as an ID in database
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean hasSpecialChar = p.matcher(uid).find();

        if (hasSpecialChar || uid.length() == 0) {
            final Toast t = Toast.makeText(getApplicationContext(),
                    "Username must be alphanumeric characters only", Toast.LENGTH_LONG);
            t.show();
            return;
        }

        final String pass = passBox.getText().toString();
        final DatabaseReference db =
                FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://project-42226.firebaseio.com/UserList");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)) {
                    final Context context = getApplicationContext();
                    final int duration = Toast.LENGTH_SHORT;
                    final Toast t = Toast.makeText(context,
                            "registration failed: userID is already in use", duration);
                    t.show();
                } else {
                    User user = UserFactory.createUser(nameBox.getText().toString(), uid, pass,
                            emailBox.getText().toString(),
                            (AccountType) accountType.getSelectedItem());

                    Log.d("newUser", user.toString());

                    db.child(uid).setValue(user);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                final Toast t = Toast.makeText(getApplicationContext(),
                        "Error registering user with database- check connectivity",
                        Toast.LENGTH_LONG);
                t.show();
            }
        });
    }
    /**
    Goes back to the main screen is cancel is pressed
     * @param view the button which is passed in
     */
    public void onCancelClicked(@SuppressWarnings("unused") View view) {
        finish();
    }


}
