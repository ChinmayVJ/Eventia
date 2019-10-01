package com.example.loginactivity.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.loginactivity.Authentication.MainActivity;
import com.example.loginactivity.Classes.UserData;
import com.example.loginactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    ImageButton back;
    ImageButton signOut;

    TextView usersName;
    TextView memberCount;
    TextView interestCount;
    Button editProfile;
    NachoTextView nachoTextView;

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
        nachoTextView = findViewById(R.id.nacho_text_view_profile);

        nachoTextView.setEnabled(false);

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

                    nachoTextView.setText(usersInterest);
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
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });

    }
}
