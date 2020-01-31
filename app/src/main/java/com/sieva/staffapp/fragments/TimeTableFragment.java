package com.sieva.staffapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sieva.staffapp.R;
import com.sieva.staffapp.adapter.PagerAdapterTimeTable;

public class TimeTableFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View timetableView = inflater.inflate(R.layout.fragment_timetable, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        final TextView toolbarmsg = timetableView.findViewById(R.id.toolbar_msg);
        ImageView
                refreshButton = timetableView.findViewById(R.id.refresh);
        ImageView
                toolbar_back_button = timetableView.findViewById(R.id.toolbar_back_button);
        String[] days = {"MON", "TUE", "WED", "THU", "FRI"};
        toolbarmsg.setText(getString(R.string.timetable));
        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = timetableView.findViewById(R.id.tab_layout);
        // tabLayout.setTabTextColors(Color.GRAY, Color.WHITE);
        // Set the text for each tab.
        for (String day : days) {
            tabLayout.addTab(tabLayout.newTab().setText(day));
        }
        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Use PagerAdapterLeaveRequest to manage page views in fragments.
        // Each page is represented by its own fragment.
        final ViewPager viewPager = timetableView.findViewById(R.id.pager);
        PagerAdapterTimeTable adapter = new PagerAdapterTimeTable(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        // Setting a listener for clicks.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return timetableView;
    }
}