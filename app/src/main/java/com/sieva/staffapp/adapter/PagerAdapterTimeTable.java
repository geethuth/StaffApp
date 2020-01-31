package com.sieva.staffapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sieva.staffapp.fragments.DayTimeTableFragment;


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
                return new DayTimeTableFragment("Monday");
            case 1:
                return new DayTimeTableFragment("Tuesday");
            case 2:
                return new DayTimeTableFragment("Wednesday");
            case 3:
                return new DayTimeTableFragment("Thursday");
            case 4:
                return new DayTimeTableFragment("Friday");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
