package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.Classes.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventInfo extends AppCompatActivity {

    TextView evGroupName;
    TextView evName;
    TextView evDate;
    TextView evTimeAndDur;
    TextView evLocation;
    TextView evHostName;
    TextView evMembers;
    TextView evDescription;
    Button joinButton;

    ImageButton backButton;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference fDatabase;

    boolean alreadyExists = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Intent intent = getIntent();
        final String event_id = intent.getStringExtra("evId");

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance().getReference();
        fDatabase.keepSynced(true);

        backButton = findViewById(R.id.toolbar_back_button);

        evGroupName = findViewById(R.id.event_group_name);
        evName = findViewById(R.id.event_name_info);
        evDate = findViewById(R.id.event_date);
        evTimeAndDur = findViewById(R.id.event_time_duration);
        evLocation = findViewById(R.id.event_location_bldg);
        evHostName = findViewById(R.id.event_host_name);
        evMembers = findViewById(R.id.number_of_members);
        evDescription = findViewById(R.id.description_data);
        joinButton = findViewById(R.id.join_group_button);

        fDatabase.child("Event Information").child(event_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EventData evData = dataSnapshot.getValue(EventData.class);
                evGroupName.setText(evData.getGroupName());
                evName.setText(evData.getEventName());
                evDate.setText(evData.getDateOfEvent());
                evTimeAndDur.setText(evData.getStartTime() + " Duration : " + evData.getDuration() + " hrs");
                evLocation.setText(evData.getAddress());
                evHostName.setText("Hosted by " + evData.getHostName());
                evMembers.setText(String.valueOf(evData.getNoOfMembers()) + " people are going");
                evDescription.setText(evData.getDescription());

                Log.i("Event ", evData.getEventName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fDatabase.child("User Information").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserData userData = dataSnapshot.getValue(UserData.class);
                        try{
                            ArrayList<String> memOfGrp = new ArrayList<>(userData.getMemberOfGroup());
                            if(!memOfGrp.contains(event_id)) {
                                alreadyExists = false;
                                memOfGrp.add(event_id);
                            }
                            userData.setMemberOfGroup(memOfGrp);
                            Log.d("Mems", String.valueOf(userData.getMemberOfGroup()));
                        }
                        catch (Exception e){
                            ArrayList<String> memOfGrp = new ArrayList<>();
                            memOfGrp.add(event_id);
                            alreadyExists = false;

                            userData.setMemberOfGroup(memOfGrp);
                            Log.d("Catch ", String.valueOf(userData.getMemberOfGroup()));
                        }
                        fDatabase.child("User Information").child(fUser.getUid()).setValue(userData);

                        fDatabase.child("User Information").child(fUser.getUid()).removeEventListener(this);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                fDatabase.child("Event Information").child(event_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        EventData evData = dataSnapshot.getValue(EventData.class);
                        if(!alreadyExists) {
                            evData.setNoOfMembers(evData.getNoOfMembers() + 1);
                            fDatabase.child("Event Information").child(event_id).setValue(evData);
                            Toast.makeText(EventInfo.this, "Group Joined", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(EventInfo.this, "Already in the Group", Toast.LENGTH_SHORT).show();
                        }

                        fDatabase.child("Event Information").child(event_id).removeEventListener(this);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
            }
        });
    }
}
