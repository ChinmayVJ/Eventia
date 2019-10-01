package com.example.loginactivity.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.loginactivity.Classes.UserData;
import com.example.loginactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity {

    NachoTextView nachoTextView;

    ImageButton backButton;
    EditText usersName;
    EditText usersAddress;
    Spinner genderSpinner;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference fDatabase;
    Query fQuery;

    String gender = "Prefer Not to Say";;
    ArrayList<String> ints = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("User Information").child(fUser.getUid());

        backButton = findViewById(R.id.toolbar_back_button_edit_profile);
        usersName = findViewById(R.id.name_entry);
        usersAddress = findViewById(R.id.address_entry);

        genderSpinner = findViewById(R.id.gender_spinner);
        nachoTextView = findViewById(R.id.nacho_text_view);

        gender = "Prefer Not to Say";
        ints = new ArrayList<>();

        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                usersName.setText(userData.getName());
                if(userData.getAddress() != ""){
                    usersAddress.setText(userData.getAddress());
                }
                if(String.valueOf(userData.getUserInterests()) != null){
                    nachoTextView.setText(userData.getUserInterests());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        gender = "Male";
                        break;
                    case 2:
                        gender = "Female";
                        break;
                    case 3:
                        gender = "Other";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender = "Prefer Not To Say";
            }
        });

        String[] items = new String[]{"- Select -", "Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        genderSpinner.setAdapter(adapter);

        String[] suggestions = new String[]{"Technology", "Tours", "Travel", "Nature", "Adventures", "Start Ups","Outdoors"};
        ArrayAdapter<String> suggestionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        nachoTextView.setAdapter(suggestionAdapter);
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoTextView.enableEditChipOnTouch(true, true);
    }

    public void afterEditProfileButtonPressed(View view){

        final String user_name = usersName.getText().toString();
        final String user_address = usersAddress.getText().toString();

        try {
            for (Chip chip : nachoTextView.getAllChips()) {
                // Do something with the data of each chip (this data will be set if the chip was created by tapping a suggestion)
                Object data = chip.getData();
                String interest = data.toString();
                Log.d("Interest is : ", interest);
                ints.add(interest);
            }
        }catch (Exception e){

        }

        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                userData.setName(user_name);
                userData.setGender(gender);
                userData.setAddress(user_address);
                userData.setUserInterests(ints);

                fDatabase.setValue(userData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
        finish();

    }
}
