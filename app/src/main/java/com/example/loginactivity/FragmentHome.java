package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.loginactivity.Classes.EventData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentHome extends Fragment {

    FloatingActionButton addEventButton;
    ImageView profileImage;

    FirebaseAuth fAuth;
    FirebaseDatabase fData;
    DatabaseReference fDatabase;

    RecyclerView dataRecyclerView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }
    @Override
    public void onViewCreated (View view, Bundle savedInstanceState)
    {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), getActivity().getSupportFragmentManager());
        final ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs =getView().findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        addEventButton = getActivity().findViewById(R.id.fab_event_adder);
        profileImage = getActivity().findViewById(R.id.profile_icon);

        fAuth = FirebaseAuth.getInstance();

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateEvent.class));
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Profile.class));

            }
        });


    }

}