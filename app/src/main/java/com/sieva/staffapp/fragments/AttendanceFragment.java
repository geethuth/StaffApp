package com.sieva.staffapp.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sieva.staffapp.R;
import com.sieva.staffapp.activity.MainActivity;
import com.sieva.staffapp.adapter.StudentDetailsAdapter;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.pojoclass.StudentListPOJO;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class AttendanceFragment extends Fragment {
    public static ArrayList<Integer> positionArray = new ArrayList<>();
    public static ArrayList<String> toggleCheckedArray = new ArrayList<>();
    public static String[] attendance;

    private ArrayList<StudentListPOJO>
            studentDetailsArray = new ArrayList<>();
    private RecyclerView
            recyclerView;
    private ProgressDialog pdia;
    private String response;
    private Button dateButton;
    private DatePickerDialog datePickerDialog;

    public AttendanceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View
                attendanceView = inflater.inflate(
                R.layout.fragment_attendance,
                container,
                false);

        final TextView toolbarmsg = attendanceView.findViewById(R.id.toolbar_msg);
        toolbarmsg.setText("Attendance");
        attendance = new String[PreferenceUtil.StudentListArray.size()];

        dateButton = attendanceView.findViewById(R.id.date);
        Button submit = attendanceView.findViewById(R.id.submitButton);
        Button cancel = attendanceView.findViewById(R.id.cancelButton);

        /*********************************************************************************************/
        dateButton.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH), month = c.get(Calendar.MONTH), year = c.get(Calendar.YEAR);
            String dt, startdate;

            @Override
            public void onClick(View view) {
                if (startdate != null) {
                    String[] date_split = startdate.split("/");
                    day = Integer.parseInt(date_split[1]);
                    month = Integer.parseInt(date_split[0]);
                    year = Integer.parseInt(date_split[2]);
                }

                // Click action
                datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), R.style.datepicker, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    Log.d("Selected Date", selectedDay + " " + selectedMonth + " " + selectedYear);
                    Date date = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.set(selectedYear, selectedMonth, selectedDay);
                    date.setTime(cal.getTime().getTime());
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format));
                    dt = dateFormat.format(date);
                    dateButton.setText(dt);
                    // fuel_values.set(0,SD.getText());
                    startdate = dateButton.getText().toString();
                    Log.d("Selected start Date", startdate);
                    System.out.println("startdate " + startdate);

                }, year, month, day);
                // Set Max date and Min date for add date
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                // datePickerDialog.setTitle("Pick start date");
                datePickerDialog.show();
            }
        });
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat curFormater = new SimpleDateFormat(getString(R.string.date_format));
        String dateString = curFormater.format(date);
        Log.d("date", dateString);
        dateButton.setText(dateString);
        /**********************************************************************************************/
        System.out.println("PreferenceUtil.studentDetailsArray: " + PreferenceUtil.StudentListArray);
        if (PreferenceUtil.StudentListArray != null) {
            recyclerView = attendanceView.findViewById(R.id.student_list);
            if (getActivity() != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            setStudentDetailsToAdapter();
        }
        //********************************************************************
        submit.setOnClickListener(view -> {

            int month = Integer.parseInt(dateButton.getText().toString().split("/+")[1]);
            int year = Integer.parseInt(dateButton.getText().toString().split("/+")[2]);

            System.out.println("positionArray: " + positionArray);
            System.out.println("toggleCheckedArray: " + toggleCheckedArray);
            ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                submitAttendanceAPI(month, year, dateButton.getText().toString(), positionArray, toggleCheckedArray);
            } else {
                Toast.makeText(getContext(), "Please check your internet connectivity..", Toast.LENGTH_SHORT).show();
            }
        });

        return attendanceView;
    }

    private void submitAttendanceAPI(int month, int year, String date, ArrayList<Integer> positionArray, ArrayList<String> toggleCheckedArray) {
        String urls = ServerUtils.ServerUrl;
        String AttendanceServerUrl = ServerUtils.submitAttendanceAPI;
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            String AttendanceListUrl = urls + AttendanceServerUrl;// Serverfilenames.loginUrl;
            Log.d("Attendance url", AttendanceListUrl);
            try {

                //$payload = "operation::submitattendance###year::2020###month::01###date::13/12/2019###present::{12,13,15,32,23}###absent::{87,10}";
                ArrayList<Pair> params = new ArrayList<>();
                Pair<String, String> pair = new Pair<>("operation", "submitattendance");
                params.add(pair);
                pair = new Pair<>("year", String.valueOf(year));
                params.add(pair);
                pair = new Pair<>("month", String.valueOf(month));
                params.add(pair);
                pair = new Pair<>("date", date);
                params.add(pair);
                pair = new Pair<>("present", "{1,3,5}");
                params.add(pair);
                pair = new Pair<>("absent", "{2,4,6,7,8}");
                params.add(pair);

                String postString = Utils.createPostString(params);
                Log.i("param", postString);
                AttendanceTask studentsListTask = new AttendanceTask();
                studentsListTask.execute(AttendanceListUrl, postString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Server connectivity issue..", Toast.LENGTH_SHORT).show();

        }

    }


    @SuppressLint("StaticFieldLeak")
    public class AttendanceTask extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getContext());
            pdia.setMessage("please wait..");
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String res = null;
            try {
                response = CustomHttpClient.executeHttpPost(url[0], url[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pdia != null && pdia.isShowing()) {
                pdia.dismiss();
            }
            System.out.println("Enable_student response: " + response);

            if (response != null && !response.equals("")) {

                if (response.contains("success")) {
                    AlertDialog SuccessAlert = new AlertDialog.Builder(Objects.requireNonNull(getContext())).create();
                    SuccessAlert.setMessage("Attendance added successfully");
                    SuccessAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialogInterface, i) -> {
                                if (getActivity() != null) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                    );
                    SuccessAlert.show();
                }
            }
        }//close onPostExecute

        @Override
        public void onCancel(DialogInterface dialogInterface) {
        }
    }

    private void setStudentDetailsToAdapter() {
        positionArray.clear();
        toggleCheckedArray.clear();
        StudentDetailsAdapter
                studentDetailsAdapter;
        if (PreferenceUtil.StudentListArray.size() > 0 && getActivity() != null) {
            studentDetailsAdapter = new StudentDetailsAdapter(getActivity(), PreferenceUtil.StudentListArray);
            recyclerView.setAdapter(studentDetailsAdapter);
            System.out.println("size: " + positionArray);
            System.out.println("size: " + positionArray.size());
        }
    }

    public static void additem(int position, String s) {
        positionArray.add(position);
        toggleCheckedArray.add(s);
    }
}