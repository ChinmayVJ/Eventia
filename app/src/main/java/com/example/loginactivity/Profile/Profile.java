package com.example.loginactivity.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.loginactivity.Authentication.MainActivity;
import com.example.loginactivity.Classes.EventAdapterDataHolder;
import com.example.loginactivity.Classes.EventAdapterDataHolderBrief;
import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.Classes.UserData;
import com.example.loginactivity.EventRelated.CreateEvent;
import com.example.loginactivity.EventRelated.EventInfo;
import com.example.loginactivity.ExtraActivities.HomePage;
import com.example.loginactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    ImageButton back;
    Toolbar toolbar;

    CircleImageView profilePic;
    TextView usersName;
    TextView usersAddress;
    TextView memberCount;
    TextView interestCount;
    NachoTextView nachoTextView;
    TextView noEvents;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference fDatabase;

    ProgressBar progressBar;
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
        progressBar = findViewById(R.id.profile_pic_loading_profile);

        profilePic = findViewById(R.id.profile_pic_my_profile);
        usersName = findViewById(R.id.text_name_profile);
        usersAddress = findViewById(R.id.text_address_profile);
        interestCount = findViewById(R.id.interest_count);
        memberCount = findViewById(R.id.member_count);
        nachoTextView = findViewById(R.id.nacho_text_view_profile);
        noEvents = findViewById(R.id.no_events_text);
        dataRecyclerView = findViewById(R.id.data_view_profile_my_events);
        eventDataArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        dataRecyclerView.setHasFixedSize( true );
        dataRecyclerView.setLayoutManager( linearLayoutManager );

        nachoTextView.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
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
                    if (!userData.getImageUrl().equals("Not uploading")) {
                        progressBar.setVisibility(View.VISIBLE);
                        Picasso.get().load(userData.getImageUrl()).into(profilePic);
                    }
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
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
                    e.printStackTrace();
                }

                try {
                    usersAddress.setText(userData.getAddress());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fDatabase.child("Event Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    EventData eventData = child.getValue(EventData.class);
                    if(eventData.getHostName().equals(userName))
                        eventDataArrayList.add(eventData);
                }

                if (eventDataArrayList.size() != 0)
                    noEvents.setVisibility(View.INVISIBLE);

                EventAdapterDataHolderBrief eventAdapterDataHolder = new EventAdapterDataHolderBrief(Profile.this, eventDataArrayList, dataRecyclerView);
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
                        startActivity(new Intent(getApplicationContext(), EditProfile.class));
                        return true;
                    case R.id.create_event_menu:
                        startActivity(new Intent(getApplicationContext(), CreateEvent.class).putExtra("status", "new"));
                        return true;
                    case R.id.logout_menu:
                        signOutClicked();
                        return true;
                    case R.id.about_us_menu:
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

    private void signOutClicked(){

        AlertDialog.Builder warningDialogBuilder = new AlertDialog.Builder(Profile.this);

        LayoutInflater layoutInflater = LayoutInflater.from(Profile.this);
        View alertView = layoutInflater.inflate(R.layout.alert_dialog_cancel, null);

        warningDialogBuilder.setView(alertView);

        Button okButton = alertView.findViewById(R.id.ok_button_alert);
        Button cancelButton = alertView.findViewById(R.id.cancel_button_alert);
        TextView alertText = alertView.findViewById(R.id.warning_text_alert);

        alertText.setText("Are you sure you want to log out ??");

        final AlertDialog warningDialog = warningDialogBuilder.create();
        warningDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finishAffinity();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warningDialog.dismiss();
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
