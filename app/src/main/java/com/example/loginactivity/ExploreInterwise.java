package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import com.example.loginactivity.Classes.DataViewHolderHome;
import com.example.loginactivity.Classes.EventData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ExploreInterwise extends AppCompatActivity {

    FirebaseDatabase fData;
    DatabaseReference fDatabase;
    Query query;

    RecyclerView dataRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_interestwise);

        Intent intent = getIntent();
        String evType = intent.getStringExtra("evType");

        fData = FirebaseDatabase.getInstance();
        fDatabase = fData.getReference().child("Event Information");
        fDatabase.keepSynced(true);

        query = fDatabase.orderByChild("interests").equalTo(evType);

        dataRecyclerView = findViewById(R.id.data_view_explore_interest);

        LinearLayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );

        layoutManager.setReverseLayout( true );
        layoutManager.setStackFromEnd( true );

        dataRecyclerView.setHasFixedSize( true );
        dataRecyclerView.setLayoutManager( layoutManager );

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<EventData, DataViewHolderHome> fAdapter = new FirebaseRecyclerAdapter<EventData, DataViewHolderHome>
                (
                        EventData.class,
                        R.layout.card_event_data,
                        DataViewHolderHome.class,
                        query
                )
        {

            @Override
            protected void populateViewHolder(DataViewHolderHome dataViewHolder, EventData eventData, int i) {
                EventData evData = eventData;
                final int position = i;

                dataViewHolder.setEventHandlerName(evData.getGroupName());
                dataViewHolder.setEventName(evData.getEventName());
                dataViewHolder.setEventTime(evData.getStartTime());
                dataViewHolder.setEventLocation(evData.getAddress());
                dataViewHolder.setNoOfMembers(Integer.toString(evData.getNoOfMembers()));

                dataViewHolder.getDataView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String post_key = getRef( position ).getKey();
                        startActivity(new Intent(getApplicationContext(), EventInfo.class).putExtra("evId", post_key));
                    }
                });
            }
        };

        dataRecyclerView.setAdapter(fAdapter);
    }
}
