package com.example.loginactivity.EventRelated;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.Classes.UserData;
import com.example.loginactivity.ExtraActivities.HomePage;
import com.example.loginactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventInfo extends AppCompatActivity {

    ImageView companyPic;
    TextView evGroupName;
    TextView evName;
    TextView evDate;
    TextView evTimeAndDur;
    TextView evLocation;
    TextView evHostName;
    TextView evMembers;
    TextView evDescription;
    Button joinButton;
    TextView cancel;

    ImageButton backButton;
    RelativeLayout editEvent;
    ProgressBar progressBar;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference fDatabase;

    boolean alreadyExists;
    String event_id;
    String hostName;
    boolean pastEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Intent intent = getIntent();
        event_id = intent.getStringExtra("evId");

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance().getReference();
        fDatabase.keepSynced(true);

        progressBar = findViewById(R.id.company_pic_loading_info);
        backButton = findViewById(R.id.toolbar_back_button);
        editEvent = findViewById(R.id.edit_event);
        companyPic = findViewById(R.id.company_pic_info);
        evGroupName = findViewById(R.id.event_group_name);
        evName = findViewById(R.id.event_name_info);
        evDate = findViewById(R.id.event_date);
        evTimeAndDur = findViewById(R.id.event_time_duration);
        evLocation = findViewById(R.id.event_location_new_event);
        evHostName = findViewById(R.id.event_host_name);
        evMembers = findViewById(R.id.number_of_members);
        evDescription = findViewById(R.id.description_data);
        joinButton = findViewById(R.id.join_group_button);
        cancel = findViewById(R.id.cancel_participation);

        pastEvent = false;
        progressBar.setVisibility(View.INVISIBLE);

        eventInformationFunction(0);
        userInformationFunction(0);

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), CreateEvent.class).putExtra("status", event_id));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userInformationFunction(1);

                eventInformationFunction(1);

                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder warningDialogBuilder = new AlertDialog.Builder(EventInfo.this);

                LayoutInflater layoutInflater = LayoutInflater.from(EventInfo.this);
                View alertView = layoutInflater.inflate(R.layout.alert_dialog_cancel, null);

                warningDialogBuilder.setView(alertView);

                final AlertDialog warningDialog = warningDialogBuilder.create();
                warningDialog.show();

                Button okButton = alertView.findViewById(R.id.ok_button_alert);
                Button cancelButton = alertView.findViewById(R.id.cancel_button_alert);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        eventInformationFunction(2);

                        userInformationFunction(2);

                        warningDialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        warningDialog.dismiss();
                    }
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void eventInformationFunction(final int choice){

        fDatabase.child("Event Information").child(event_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EventData evData = dataSnapshot.getValue(EventData.class);
                if (choice == 0){
                    evGroupName.setText(evData.getGroupName());
                    evName.setText(evData.getEventName());
                    evDate.setText(evData.getDateOfEvent());
                    evTimeAndDur.setText(evData.getStartTime() + " Duration : " + evData.getDuration() + " hrs");
                    evLocation.setText(evData.getAddress());
                    hostName = evData.getHostName();
                    evHostName.setText("Hosted by " + hostName);
                    evMembers.setText(evData.getNoOfMembers() + " people are going");
                    evDescription.setText(evData.getDescription());
                    try {
                        String uri = evData.getImageUrl();
                        if(!uri.equals("Not uploading")) {
                            progressBar.setVisibility(View.VISIBLE);
                            Picasso.get().load(uri).into(companyPic);
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date strDate = new Date();
                    try {
                        strDate = sdf.parse(evData.getDateOfEvent());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (new Date().after(strDate)) {
                        joinButton.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.INVISIBLE);
                        pastEvent = true;
                    }

                }
                else if (choice == 1){
                    if(!alreadyExists) {
                        evData.setNoOfMembers(evData.getNoOfMembers() + 1);
                        fDatabase.child("Event Information").child(event_id).setValue(evData);
                        Toast.makeText(EventInfo.this, "Response Recorded", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(EventInfo.this, "Response Already Recorded", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (choice == 2){
                    evData.setNoOfMembers(evData.getNoOfMembers() - 1);
                    fDatabase.child("Event Information").child(event_id).setValue(evData);
                }

                fDatabase.child("Event Information").child(event_id).removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void userInformationFunction(final int choice){

        fDatabase.child("User Information").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);

                if (choice == 0){
                    try{
                        ArrayList<String> memOfGrp = new ArrayList<>(userData.getMemberOfGroup());
                        if(memOfGrp.contains(event_id)) {
                            alreadyExists = true;
                            if(!pastEvent) {
//                                joinButton.setText("You are Going");
//                                joinButton.setEnabled(false);
                                joinButton.setVisibility(View.INVISIBLE);
                                cancel.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            alreadyExists = false;
                            cancel.setVisibility(View.INVISIBLE);
                        }
                    }
                    catch (Exception e){
                        alreadyExists = false;
                        cancel.setVisibility(View.INVISIBLE);
                    }
                    if(hostName.equals(userData.getName()) && !pastEvent){
                        editEvent.setVisibility(View.VISIBLE);
                    }
                    else{
                        editEvent.setVisibility(View.INVISIBLE);
                    }
                }
                else if (choice == 1){
                    try{
                        ArrayList<String> memOfGrp = new ArrayList<>(userData.getMemberOfGroup());
                        if(!memOfGrp.contains(event_id)) {
                            alreadyExists = false;
                            memOfGrp.add(event_id);
                        }
                        userData.setMemberOfGroup(memOfGrp);
                    }
                    catch (Exception e){
                        ArrayList<String> memOfGrp = new ArrayList<>();
                        memOfGrp.add(event_id);
                        alreadyExists = false;
                        userData.setMemberOfGroup(memOfGrp);
                    }
                    fDatabase.child("User Information").child(fUser.getUid()).setValue(userData);
                }
                else if (choice == 2){
                    try{
                        ArrayList<String> memOfGrp = new ArrayList<>(userData.getMemberOfGroup());
                        memOfGrp.remove(event_id);
                        userData.setMemberOfGroup(memOfGrp);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    fDatabase.child("User Information").child(fUser.getUid()).setValue(userData);
                }

                fDatabase.child("User Information").child(fUser.getUid()).removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
