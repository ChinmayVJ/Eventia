package com.example.loginactivity.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.loginactivity.Authentication.MainActivity;
import com.example.loginactivity.Classes.EventAdapterDataHolder;
import com.example.loginactivity.Classes.EventAdapterDataHolderBrief;
import com.example.loginactivity.Classes.EventData;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Profile extends AppCompatActivity {

    ImageButton back;
    Toolbar toolbar;

    TextView usersName;
    TextView memberCount;
    TextView interestCount;
    NachoTextView nachoTextView;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference fDatabase;

    RecyclerView dataRecyclerView;
    ArrayList<String> usersInterest;
    ArrayList<String> memberOfGroup;
    LinearLayoutManager linearLayoutManager;
    ArrayList<EventData> eventDataArrayList;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back = findViewById(R.id.toolbar_back_button_profile);
        toolbar = findViewById(R.id.toolbar_profile);
        usersName = findViewById(R.id.text_name_profile);
        interestCount = findViewById(R.id.interest_count);
        memberCount = findViewById(R.id.member_count);
        nachoTextView = findViewById(R.id.nacho_text_view_profile);
        dataRecyclerView = findViewById(R.id.data_view_profile_my_events);
        eventDataArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        dataRecyclerView.setHasFixedSize( true );
        dataRecyclerView.setLayoutManager( linearLayoutManager );

        nachoTextView.setEnabled(false);
        toolbar.inflateMenu(R.menu.profile_options);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance().getReference();

        fDatabase.child("User Information").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                userName = userData.getName();
                usersName.setText(userName);
                try {
                    usersInterest = new ArrayList<>(userData.getUserInterests());
                    interestCount.setText(String.valueOf(usersInterest.size()));

                    nachoTextView.setText(usersInterest);
                }
                catch (Exception e){
                    e.printStackTrace();
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

        fDatabase.child("Event Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (memberOfGroup.size() != 0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        EventData eventData = child.getValue(EventData.class);
                        if(eventData.getHostName().equals(userName))
                            eventDataArrayList.add(eventData);
                    }
                }

                EventAdapterDataHolderBrief eventAdapterDataHolder = new EventAdapterDataHolderBrief(getApplicationContext(), eventDataArrayList, dataRecyclerView);
                dataRecyclerView.setAdapter(eventAdapterDataHolder);

                fDatabase.child("Event Information").removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_profile_menu:
                        Log.e("working", "Settings");
                        startActivity(new Intent(getApplicationContext(), EditProfile.class));
                        return true;
                    case R.id.logout_menu:
                        Log.e("working", "Log Out");
                        fAuth.signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finishAffinity();
                        return true;
                    case R.id.about_us_menu:
                        Log.e("working", "About Us");
                        return true;
                }
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.profile_options, menu );

        return super.onCreateOptionsMenu( menu );
    }

}
