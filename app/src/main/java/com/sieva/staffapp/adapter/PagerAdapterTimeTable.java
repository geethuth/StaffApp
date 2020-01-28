package com.sieva.staffapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sieva.staffapp.fragments.timetabletabs.FridayFragment;
import com.sieva.staffapp.fragments.timetabletabs.MondayFragment;
import com.sieva.staffapp.fragments.timetabletabs.ThursdayFragment;
import com.sieva.staffapp.fragments.timetabletabs.TuesdayFragment;
import com.sieva.staffapp.fragments.timetabletabs.WednesdayFragment;


public class PagerAdapterTimeTable extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterTimeTable(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MondayFragment();
            case 1:
                return new TuesdayFragment();
            case 2:
                return new WednesdayFragment();
            case 3:
                return new ThursdayFragment();
            case 4:
                return new FridayFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
