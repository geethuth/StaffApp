package com.sieva.staffapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sieva.staffapp.fragments.leaverequesttabs.TabApprovedFragment;
import com.sieva.staffapp.fragments.leaverequesttabs.TabRejectedFragment;
import com.sieva.staffapp.fragments.leaverequesttabs.TabPendingFragment;


public class PagerAdapterLeaveRequest extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterLeaveRequest(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TabPendingFragment();
            case 1:
                return new TabApprovedFragment();
            case 2:
                return new TabRejectedFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
