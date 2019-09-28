package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.loginactivity.Classes.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    ImageButton back;
    ImageButton signOut;

    TextView usersName;
    TextView address;
    TextView memberCount;
    TextView interestCount;
    Button editProfile;
    ListView interList;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference fDatabase;

    ArrayList<String> usersInterest;
    ArrayList<String> memberOfGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back = findViewById(R.id.toolbar_back_button_profile);
        signOut = findViewById(R.id.toolbar_logout_button_profile);
        usersName = findViewById(R.id.text_name_profile);
        editProfile = findViewById(R.id.button_edit_profile);
        interestCount = findViewById(R.id.interest_count);
        memberCount = findViewById(R.id.member_count);
        interList = findViewById(R.id.interests_list_view);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("User Information").child(fUser.getUid());

        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                usersName.setText(userData.getName());
                try {
                    usersInterest = new ArrayList<>(userData.getUserInterests());
                    interestCount.setText(String.valueOf(usersInterest.size()));

                    ArrayAdapter<String> interAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, usersInterest);
                    interList.setAdapter(interAdapter);
                }
                catch (Exception e){

                }

                try{
                    memberOfGroup = new ArrayList<>(userData.getMemberOfGroup());
                    memberCount.setText(String.valueOf(memberOfGroup.size()));
                }
                catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
                finish();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EditProfile.class));
                finish();
            }
        });

    }
}
