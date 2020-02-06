package com.sieva.staffapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sieva.staffapp.R;
import com.sieva.staffapp.adapter.PagerAdapterLeaveRequest;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.Utils;

import static com.sieva.staffapp.fragments.leaverequesttabs.TabApprovedFragment.approvedRequestAPI;
import static com.sieva.staffapp.fragments.leaverequesttabs.TabPendingFragment.pendingRequestAPI;
import static com.sieva.staffapp.fragments.leaverequesttabs.TabRejectedFragment.rejectedRequestAPI;


public class LeaveFragment extends Fragment {

    Context context;
    ImageView
            refreshButton;

    //TextView noData;
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
        ImageView
                toolbar_back_button = leaveview.findViewById(R.id.toolbar_back_button);
        toolbar_back_button.setVisibility(View.VISIBLE);
        if (getActivity() != null) {
            toolbar_back_button.setOnClickListener((View v) -> getActivity().onBackPressed());
        }

        toolbarText.setText("Leave Requests");

        refreshButton = leaveview.findViewById(R.id.refresh);
        refreshButton.setVisibility(View.VISIBLE);
        refreshButton.setOnClickListener(view -> {
            if (getActivity() != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    if (PreferenceUtil.classDetailsArray != null) {
                        approvedRequestAPI();
                        pendingRequestAPI();
                        rejectedRequestAPI();
                    } else {
                        Utils.showAlertMessage(getActivity(), "Class details is currently unavailable");
                    }

                } else {
                    Toast.makeText(getContext(), getString(R.string.internet_connectivity_check), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
