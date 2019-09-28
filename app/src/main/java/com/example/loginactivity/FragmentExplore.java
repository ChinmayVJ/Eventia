package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.Classes.DataViewHolderHome;
import com.example.loginactivity.Classes.EventData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FragmentExplore extends Fragment {

    FirebaseDatabase fData;
    DatabaseReference fDatabase;
    Query query;

    RecyclerView dataRecyclerView;
    CardView exploreCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        exploreCard = getView().findViewById(R.id.rw_00);

        fData = FirebaseDatabase.getInstance();
        fDatabase = fData.getReference().child("Event Information");
        fDatabase.keepSynced(true);

        query = fDatabase.orderByChild("category").equalTo("Film");

        dataRecyclerView = getActivity().findViewById(R.id.social_events_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity(), LinearLayoutManager.HORIZONTAL, true );

        layoutManager.setReverseLayout( true );
        layoutManager.setStackFromEnd( true );

        dataRecyclerView.setHasFixedSize( true );
        dataRecyclerView.setLayoutManager( layoutManager );

//        dataRecyclerView.setVisibility(View.INVISIBLE);

//        exploreCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dataRecyclerView.setVisibility(View.VISIBLE);
//            }
//        });
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
                        startActivity(new Intent(getActivity(), EventInfo.class).putExtra("evId", post_key));
                    }
                });
            }
        };

        dataRecyclerView.setAdapter(fAdapter);
    }

}
