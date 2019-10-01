package com.example.loginactivity.ExtraActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.loginactivity.Classes.DataViewHolderHome;
import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.EventRelated.EventInfo;
import com.example.loginactivity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ExploreCategory extends AppCompatActivity {

    TextView toolbarTitle;
    ImageButton backButton;

    FirebaseDatabase fData;
    DatabaseReference fDatabase;
    Query query;

    RecyclerView dataRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_interestwise);

        toolbarTitle = findViewById(R.id.custom_toolbar_title);
        backButton = findViewById(R.id.custom_toolbar_back_button);

        Intent intent = getIntent();
        String evType = intent.getStringExtra("evType");

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
                        String post_key = getRef(position).getKey();
                        startActivity(new Intent(getApplicationContext(), EventInfo.class).putExtra("evId", post_key));
                    }
                });
            }
        };

        dataRecyclerView.setAdapter(fAdapter);
    }
}
