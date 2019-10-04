package com.example.loginactivity.ExtraActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.loginactivity.Classes.EventAdapterDataHolder;
import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExploreCategory extends AppCompatActivity {

    TextView toolbarTitle;
    ImageButton backButton;

    FirebaseDatabase fData;
    DatabaseReference fDatabase;
    Query query;

    RecyclerView dataRecyclerView;
    ArrayList<EventData> eventDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_interestwise);

        toolbarTitle = findViewById(R.id.custom_toolbar_title);
        backButton = findViewById(R.id.custom_toolbar_back_button);

        Intent intent = getIntent();
        final String evType = intent.getStringExtra("evType");

        toolbarTitle.setText(evType);

        fData = FirebaseDatabase.getInstance();
        fDatabase = fData.getReference().child("Event Information");
        fDatabase.keepSynced(true);

        query = fDatabase.orderByChild("category").equalTo(evType);

        dataRecyclerView = findViewById(R.id.data_view_explore_interest);
        LinearLayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        layoutManager.setReverseLayout( true );
        layoutManager.setStackFromEnd( true );
        dataRecyclerView.setHasFixedSize( true );
        dataRecyclerView.setLayoutManager( layoutManager );

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        eventDataArrayList = new ArrayList<>();
        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    EventData eventData = child.getValue(EventData.class);
                    if (eventData.getCategory().equals(evType)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date strDate = new Date();
                        try {
                            strDate = sdf.parse(eventData.getDateOfEvent());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date todayDate = new Date();
                        if (todayDate.before(strDate) || (todayDate.getDate() == strDate.getDate()
                                && todayDate.getMonth() == strDate.getMonth()
                                && todayDate.getYear() == strDate.getYear()))
                        {
                            eventDataArrayList.add(eventData);
                        }
                    }
                }

                EventAdapterDataHolder eventAdapterDataHolder = new EventAdapterDataHolder(getApplicationContext(), eventDataArrayList, dataRecyclerView);
                dataRecyclerView.setAdapter(eventAdapterDataHolder);

                fDatabase.child("Event Information").removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
