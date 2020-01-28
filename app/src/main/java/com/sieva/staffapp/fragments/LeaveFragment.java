package com.sieva.staffapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sieva.staffapp.R;
import com.sieva.staffapp.adapter.PagerAdapterLeaveRequest;


public class LeaveFragment extends Fragment {
    private RecyclerView
            recyclerView;
    Context context;

    public LeaveFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View leaveview = inflater.inflate(
                R.layout.fragment_leave,
                container,
                false);

        /**********************************************************************************************/

        context = getContext();

        /**********************************************************************************************/
        TextView
                toolbarText = leaveview.findViewById(R.id.toolbar_msg);


        toolbarText.setText("Leave Requests");


        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = leaveview.findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(Color.GRAY, Color.BLACK);
        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Approved"));
        tabLayout.addTab(tabLayout.newTab().setText("Rejected"));
        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Use PagerAdapterLeaveRequest to manage page views in fragments.
        // Each page is represented by its own fragment.
        final ViewPager viewPager = leaveview.findViewById(R.id.pager);
        PagerAdapterLeaveRequest adapter = new PagerAdapterLeaveRequest(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
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

        return leaveview;
    }
}
