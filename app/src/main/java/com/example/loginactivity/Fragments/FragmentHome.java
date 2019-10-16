package com.example.loginactivity.Fragments;

import android.content.Intent;
import android.os.Bundle;

import com.example.loginactivity.EventRelated.CreateEvent;
import com.example.loginactivity.Profile.Profile;
import com.example.loginactivity.R;
import com.example.loginactivity.main.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentHome extends Fragment {

    ImageView profileImage;

    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    TabLayout tabs;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        tabs = root.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        profileImage = root.findViewById(R.id.profile_icon);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Profile.class));
            }
        });

        return root;
    }

}