package com.example.loginactivity.home_tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.Classes.DataViewHolder;
import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllTab extends Fragment{

    FirebaseAuth fAuth;
    FirebaseDatabase fData;
    DatabaseReference fDatabase;

    RecyclerView dataRecyclerView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.all_tab, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fData = FirebaseDatabase.getInstance();
        fDatabase = fData.getReference().child("Event Information");
        fDatabase.keepSynced(true);

        dataRecyclerView = getView().findViewById(R.id.data_view_all_tab);

        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );

        layoutManager.setReverseLayout( true );
        layoutManager.setStackFromEnd( true );

        dataRecyclerView.setHasFixedSize( true );
        dataRecyclerView.setLayoutManager( layoutManager );

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<EventData, DataViewHolder> fAdapter = new FirebaseRecyclerAdapter<EventData, DataViewHolder>
                (
                        EventData.class,
                        R.layout.event_data_card,
                        DataViewHolder.class,
                        fDatabase
                )
        {

            @Override
            protected void populateViewHolder(DataViewHolder dataViewHolder, EventData eventData, int i) {
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
                        Toast.makeText(getActivity(), "Clicked on the Item Card : " + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        dataRecyclerView.setAdapter(fAdapter);
    }

}
