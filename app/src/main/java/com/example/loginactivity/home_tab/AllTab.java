package com.example.loginactivity.home_tab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.Classes.DataViewHolderHome;
import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.EventInfo;
import com.example.loginactivity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllTab extends Fragment{

    FirebaseDatabase fData;
    DatabaseReference fDatabase;

    RecyclerView dataRecyclerView;

    boolean ready = false;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_all_events, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataRecyclerView = getView().findViewById(R.id.data_view_all_tab);

//      LinearLayoutManager layoutManager = new LinearLayoutManager( getContext(), LinearLayoutManager.HORIZONTAL, true );

        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setReverseLayout( true );
        layoutManager.setStackFromEnd( true );
        dataRecyclerView.setHasFixedSize( true );
        dataRecyclerView.setLayoutManager( layoutManager );

        ready = true;

    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            Log.d("All Tab ", "Fragment is visible.");
            if(ready)
                activateOnAllTabSelect();
        }
        else
            Log.d("All Tab ", "Fragment is not visible.");
    }*/

    @Override
    public void onStart() {
        super.onStart();

        fData = FirebaseDatabase.getInstance();
        fDatabase = fData.getReference().child("Event Information");
        fDatabase.keepSynced(true);

        FirebaseRecyclerAdapter<EventData, DataViewHolderHome> fAdapter = new FirebaseRecyclerAdapter<EventData, DataViewHolderHome>
                (
                        EventData.class,
                        R.layout.card_event_data,
                        DataViewHolderHome.class,
                        fDatabase
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

                Log.d("Working", "all tab adapter works properly");

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
