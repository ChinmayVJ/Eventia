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

import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

                dataViewHolder.setEventHandlerName(evData.getGroupName());
                dataViewHolder.setEventName(evData.getEventName());
                dataViewHolder.setEventTime(evData.getStartTime());
                dataViewHolder.setEventLocation(evData.getAddress());
                dataViewHolder.setNoOfMembers(Integer.toString(evData.getNoOfMembers()));

                dataViewHolder.dataView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Clicked on the Item Card", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        dataRecyclerView.setAdapter(fAdapter);
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder{

        View dataView;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            dataView = itemView;
        }

        public void setEventHandlerName(String eventHandlerName){
            TextView event_handler_name = dataView.findViewById(R.id.event_handler_name);
            event_handler_name.setText(eventHandlerName);
        }

        public void setEventName(String eventName){
            TextView event_name = dataView.findViewById(R.id.event_name);
            event_name.setText(eventName);
        }

        public void setEventTime(String eventTime){
            TextView event_time = dataView.findViewById(R.id.event_time);
            event_time.setText(eventTime);
        }

        public void setEventLocation(String eventLocation){
            TextView event_location = dataView.findViewById(R.id.event_location);
            event_location.setText(eventLocation);
        }

        public void setNoOfMembers(String noOfMembers){
            TextView no_of_members = dataView.findViewById(R.id.no_of_mems);
            no_of_members.setText(noOfMembers);
        }
    }

}
