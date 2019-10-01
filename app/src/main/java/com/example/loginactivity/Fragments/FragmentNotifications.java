package com.example.loginactivity.Fragments;

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
import com.example.loginactivity.Classes.EventAdapterDataHolder;
import com.example.loginactivity.Classes.EventData;
import com.example.loginactivity.Classes.UserData;
import com.example.loginactivity.EventRelated.EventInfo;
import com.example.loginactivity.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

public class FragmentNotifications extends Fragment {

    FirebaseAuth fAuth;
    FirebaseDatabase fData;
    DatabaseReference fDatabase;

    RecyclerView dataRecyclerView;
    ArrayList<EventData> eventDataArrayList;
    ArrayList<String> eventIds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();
        fDatabase = fData.getReference();
        fDatabase.keepSynced(true);
        Log.e("working", "going tab working in onViewCreated");

        dataRecyclerView = root.findViewById(R.id.data_view_notification_fragment);

        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setReverseLayout( true );
        layoutManager.setStackFromEnd( true );
        dataRecyclerView.setHasFixedSize( true );
        dataRecyclerView.setLayoutManager( layoutManager );

        eventDataArrayList = new ArrayList<>();
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
                            if (todayDate.getDate() == strDate.getDate()
                            && todayDate.getMonth() == strDate.getMonth()
                            && todayDate.getYear() == strDate.getYear()) {
//                                your_date_is_outdated = true;
                                eventDataArrayList.add(eventData);
                            }
                        }
                    }
                }

                EventAdapterDataHolder eventAdapterDataHolder = new EventAdapterDataHolder(getContext(), eventDataArrayList, dataRecyclerView);
                dataRecyclerView.setAdapter(eventAdapterDataHolder);

                fDatabase.child("Event Information").removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }
}
