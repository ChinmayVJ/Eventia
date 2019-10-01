package com.example.loginactivity.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.loginactivity.Tabs.AllTab;
import com.example.loginactivity.Tabs.GoingTab;
import com.example.loginactivity.Tabs.PastTab;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new AllTab();
        }
        else if (position == 1)
        {
            fragment = new GoingTab();
        }
        else if (position == 2)
        {
            fragment = new PastTab();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "All";
        }
        else if (position == 1)
        {
            title = "Going";
        }
        else if (position == 2)
        {
            title = "Past";
        }
        return title;
    }
}
