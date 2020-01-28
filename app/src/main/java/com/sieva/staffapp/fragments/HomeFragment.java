package com.sieva.staffapp.fragments;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sieva.staffapp.R;
import com.sieva.staffapp.activity.ClassTeacherActivity;
import com.sieva.staffapp.adapter.SubjectDetailsAdapter;
import com.sieva.staffapp.helper.StudentListDetailsHelper;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView
            recyclerView;
    private ProgressDialog pdia;
    private String response;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView staffName = root.findViewById(R.id.staffname);
        final TextView schoolName = root.findViewById(R.id.schoolname);
        final TextView contactDetails = root.findViewById(R.id.contactdetails);
        final TextView className = root.findViewById(R.id.classPhoto);
        final TextView category = root.findViewById(R.id.category);
        final TextView classId = root.findViewById(R.id.classId);
        final TextView division = root.findViewById(R.id.division);
        CardView classcardview = root.findViewById(R.id.classcardview);
        LinearLayout subjectLayout = root.findViewById(R.id.subjects_layout);
        LinearLayout attendanceLayout = root.findViewById(R.id.attendancelayout);
        LinearLayout leaveLayout = root.findViewById(R.id.leavelayout);

        if (PreferenceUtil.staffDetailsArray != null) {
            staffName.setText(PreferenceUtil.staffDetailsArray.get(0).getStaffName() + "  |  " + "ID: " + PreferenceUtil.staffDetailsArray.get(0).getStaffId());
            schoolName.setText(PreferenceUtil.staffDetailsArray.get(0).getSchoolName());
            contactDetails.setText(PreferenceUtil.staffDetailsArray.get(0).getEmail() + "  |  " + PreferenceUtil.staffDetailsArray.get(0).getPhoneNo());
        }
        if (PreferenceUtil.classDetailsArray != null) {
            className.setText(PreferenceUtil.classDetailsArray.get(0).getClassName());
            category.setText("Category : Class Teacher");
            classId.setText("Class ID: " + PreferenceUtil.classDetailsArray.get(0).getClassId());
            division.setText("Division: " + PreferenceUtil.classDetailsArray.get(0).getClassDivision());
        } else {
            classcardview.setVisibility(View.GONE);
        }
        if (PreferenceUtil.subjectDetailsArray != null) {
            recyclerView = root.findViewById(R.id.subject_list);

            if (getActivity() != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            setSubjectDetailsToAdapter();

        } else {
            subjectLayout.setVisibility(View.GONE);
        }
        attendanceLayout.setOnClickListener(clickedView -> {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                studentListAPI();
            } else {
                Toast.makeText(getContext(), "Please check your internet connectivity..", Toast.LENGTH_SHORT).show();
            }
        });
        leaveLayout.setOnClickListener(clickedView -> {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                // leaveListAPI();
                Intent intent = new Intent(getActivity(), ClassTeacherActivity.class);
                intent.putExtra("selectedFragment", "leave");
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Please check your internet connectivity..", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    private void leaveListAPI() {
    }

    private void studentListAPI() {

        String urls = ServerUtils.ServerUrl;
        String loginServerUrl = ServerUtils.FetchstudentlistApi;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            String studentListUrl = urls + loginServerUrl;// Serverfilenames.loginUrl;
            Log.d("studentListUrl url", studentListUrl);
            try {
                ArrayList<Pair> params = new ArrayList<>();
                Pair<String, String> pair = new Pair<>("operation", "studentlist");
                params.add(pair);
                pair = new Pair<>("classid", PreferenceUtil.classDetailsArray.get(0).getClassId());
                params.add(pair);

                String postString = Utils.createPostString(params);
                Log.i("param", postString);
                StudentsListTask studentsListTask = new StudentsListTask();
                studentsListTask.execute(studentListUrl, postString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Server connectivity issue..", Toast.LENGTH_SHORT).show();

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class StudentsListTask extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getContext());
            pdia.setMessage("Loading..");
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
                JSONObject respjson = new JSONObject();
                try {
                    respjson = new JSONObject(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    //Log.d(TAG, response);
                    if (response != null && response.contains("success")) {
                        try {
                            if (response.contains("data")) {
                                JSONArray
                                        studentArray = respjson.getJSONArray("data");
                                PreferenceUtil.StudentListArray = StudentListDetailsHelper.getStudentList(studentArray.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AlertDialog WrongPasswordAlert = new AlertDialog.Builder(getContext()).create();
                        WrongPasswordAlert.setMessage("No data available now");
                        WrongPasswordAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                (dialog, which) -> dialog.dismiss());
                        WrongPasswordAlert.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            // System.out.println("test response: " + PreferenceUtil.StudentListArray.get(0).getStudentName());
            if (PreferenceUtil.StudentListArray != null) {
                if (PreferenceUtil.StudentListArray.size() > 0) {
                    if (getActivity() != null) {
                        Intent intent = new Intent(getActivity(), ClassTeacherActivity.class);
                        intent.putExtra("selectedFragment", "attendance");
                        startActivity(intent);
                    }
                }

            } else {
                Toast.makeText(getContext(), "Student list is not available", Toast.LENGTH_SHORT).show();
            }
        }//close onPostExecute

        @Override
        public void onCancel(DialogInterface dialogInterface) {
        }
    }

    private void setSubjectDetailsToAdapter() {
        SubjectDetailsAdapter
                studentDetailsAdapter;
        if (PreferenceUtil.subjectDetailsArray.size() > 0 && getActivity() != null) {
            studentDetailsAdapter = new SubjectDetailsAdapter(getActivity(), PreferenceUtil.subjectDetailsArray);
            recyclerView.setAdapter(studentDetailsAdapter);
        }
    }

}