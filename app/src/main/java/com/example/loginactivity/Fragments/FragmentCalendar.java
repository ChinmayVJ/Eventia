package com.example.loginactivity.Fragments;

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

import com.example.loginactivity.Classes.EventAdapterDataHolderBrief;
import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.Classes.UserData;
import com.example.loginactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentCalendar extends Fragment {

    FirebaseAuth fAuth;
    FirebaseDatabase fData;
    DatabaseReference fDatabase;

    RecyclerView dataRecyclerViewToday;
    RecyclerView dataRecyclerViewTomorrow;
    ArrayList<EventData> eventDataArrayListToday;
    ArrayList<EventData> eventDataArrayListTomorrow;
    ArrayList<String> eventIds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();
        fDatabase = fData.getReference();
        fDatabase.keepSynced(true);
        Log.e("working", "going tab working in onViewCreated");

        dataRecyclerViewToday = root.findViewById(R.id.data_view_notification_fragment_today);
        dataRecyclerViewTomorrow = root.findViewById(R.id.data_view_notification_fragment_tomorrow);

        LinearLayoutManager layoutManagerToday = new LinearLayoutManager( getContext() );
        layoutManagerToday.setReverseLayout( true );
        layoutManagerToday.setStackFromEnd( true );
        dataRecyclerViewToday.setHasFixedSize( true );
        dataRecyclerViewToday.setLayoutManager( layoutManagerToday );

        LinearLayoutManager layoutManagerTomorrow = new LinearLayoutManager( getContext() );
        layoutManagerTomorrow.setReverseLayout( true );
        layoutManagerTomorrow.setStackFromEnd( true );
        dataRecyclerViewTomorrow.setHasFixedSize( true );
        dataRecyclerViewTomorrow.setLayoutManager( layoutManagerTomorrow );

        eventIds = new ArrayList<>();
        fDatabase.child("User Information").child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                try {
                    eventIds = new ArrayList<>(userData.getMemberOfGroup());
                }
                catch (Exception e) {
                }
                fDatabase.child("User Information").child(fAuth.getCurrentUser().getUid()).removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        eventDataArrayListToday = new ArrayList<>();
        eventDataArrayListTomorrow = new ArrayList<>();
        fDatabase.child("Event Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (eventIds.size() != 0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String tempId = child.getKey();
                        if (eventIds.contains(tempId)) {
                            EventData eventData = child.getValue(EventData.class);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date strDate = new Date();
                            try {
                                strDate = sdf.parse(eventData.getDateOfEvent());
                                Log.e("strDate", strDate.toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date todayDate = new Date();
                            Log.e("currentDate", todayDate.toString());
                            if (todayDate.getMonth() == strDate.getMonth()
                            && todayDate.getYear() == strDate.getYear()) {

                                if(todayDate.getDate() == strDate.getDate())
                                    eventDataArrayListToday.add(eventData);
                                else if (todayDate.getDate() + 1 == strDate.getDate())
                                    eventDataArrayListTomorrow.add(eventData);
                            }
                        }
                    }
                }

                EventAdapterDataHolderBrief eventAdapterDataHolderToday = new EventAdapterDataHolderBrief(getContext(), eventDataArrayListToday, dataRecyclerViewToday);
                dataRecyclerViewToday.setAdapter(eventAdapterDataHolderToday);

                EventAdapterDataHolderBrief eventAdapterDataHolderTomorrow = new EventAdapterDataHolderBrief(getContext(), eventDataArrayListTomorrow, dataRecyclerViewTomorrow);
                dataRecyclerViewTomorrow.setAdapter(eventAdapterDataHolderTomorrow);

                fDatabase.child("Event Information").removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }
}
