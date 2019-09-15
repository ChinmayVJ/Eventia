package com.example.loginactivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.MenuItem;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        homeToolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(homeToolbar);
        getSupportActionBar().setTitle("MeetUp");

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.navigation_home:
                    selectedFragment = new FragmentHome();
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = new FragmentExplore();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = new FragmentNotifications();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

}
