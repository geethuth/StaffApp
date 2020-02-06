package com.sieva.staffapp.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sieva.staffapp.R;
import com.sieva.staffapp.fragments.AttendanceFragment;
import com.sieva.staffapp.fragments.ChangePasswordFragment;
import com.sieva.staffapp.fragments.LeaveFragment;

public class ClassTeacherActivity extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_teacher);
        getSupportActionBar().hide();


        Intent
                intent = getIntent();
        String
                selectedFragment = intent.getStringExtra("selectedFragment");
        if (selectedFragment != null) {
            if (selectedFragment.equals("attendance")) {
                Fragment
                        attendanceFragment = new AttendanceFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, attendanceFragment,
                                attendanceFragment.getClass().getSimpleName())
                        .addToBackStack(null).commit();

            }
            if (selectedFragment.equals("leave")) {
                Fragment
                        leaveFragment = new LeaveFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, leaveFragment,
                                leaveFragment.getClass().getSimpleName())
                        .addToBackStack(null).commit();

            }
            if (selectedFragment.equals("settings")) {
                Fragment
                        changePasswordFragment = new ChangePasswordFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, changePasswordFragment,
                                changePasswordFragment.getClass().getSimpleName())
                        .addToBackStack(null).commit();

            }
        }
    }

    /**********************************************************************************************/
    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);

        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        System.out.println("fragments: " + fragments);
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
}
