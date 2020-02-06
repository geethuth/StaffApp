package com.sieva.staffapp.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sieva.staffapp.R;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.pojoclass.SubjectPOJO;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MarkentryFragment extends Fragment {

    private Spinner std_spinner, subject_spinner, exam_spinner;
    private String std, subject, exam, response;
    private ProgressDialog pdia;
    private View root;
    private TextView nodata;
    private ScrollView scrollDynamic;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_markentry, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        final TextView toolbarmsg = root.findViewById(R.id.toolbar_msg);
        std_spinner = root.findViewById(R.id.std_spinner);
        subject_spinner = root.findViewById(R.id.subject_spinner);
        exam_spinner = root.findViewById(R.id.exam_spinner);
        nodata = root.findViewById(R.id.noData);
        scrollDynamic = root.findViewById(R.id.dynamicScroll);

        AdapterView.OnItemSelectedListener listner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (std_spinner.getSelectedItem() != null)
                    std = std_spinner.getSelectedItem().toString();
                if (subject_spinner.getSelectedItem() != null)
                    subject = subject_spinner.getSelectedItem().toString();
                if (exam_spinner.getSelectedItem() != null)
                    exam = exam_spinner.getSelectedItem().toString();

                if (std != null && subject != null && exam != null) {
                    // marklistAPI(std, subject, exam);
                    markListAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        std_spinner.setOnItemSelectedListener(listner);
        subject_spinner.setOnItemSelectedListener(listner);
        exam_spinner.setOnItemSelectedListener(listner);

        toolbarmsg.setText("Mark List");
        ArrayList<String> division_details = new ArrayList<>();
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> exams = new ArrayList<>();
        if (PreferenceUtil.subjectDetailsArray != null) {
            for (int i = 0; i < PreferenceUtil.subjectDetailsArray.size(); i++) {
                SubjectPOJO subjectPOJO = PreferenceUtil.subjectDetailsArray.get(i);
                division_details.add(subjectPOJO.getSubjectClassName() + " - " + subjectPOJO.getSubjectClassDivision());
                subjects.add(subjectPOJO.getSubjectName());
            }
            String[] division = division_details.toArray(new String[division_details.size()]);
            String[] subject = subjects.toArray(new String[subjects.size()]);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, division);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            std_spinner.setAdapter(adapter);

            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subject);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subject_spinner.setAdapter(adapter);
        }
        std = std_spinner.getSelectedItem().toString();
        subject = subject_spinner.getSelectedItem().toString();

        Log.d("mark#entry", "##" + std + "  ### " + subject);

        getExamDetails(std, subject);
        return root;
    }

    private void marklistAPI(String std, String subject, String exam) {
        String urls = ServerUtils.ServerUrl;
        String MarklistServerUrl = ServerUtils.submitAttendanceAPI;
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            String MarkListUrl = urls + MarklistServerUrl;// Serverfilenames.loginUrl;
            Log.d("Attendance url", MarklistServerUrl);
            try {
                //$payload = "operation::submitattendance###year::2020###month::01###date::13/12/2019###present::{12,13,15,32,23}###absent::{87,10}";
                ArrayList<Pair> params = new ArrayList<>();
                Pair<String, String> pair = new Pair<>("operation", "submitattendance");
                params.add(pair);
                pair = new Pair<>("class", std);
                params.add(pair);
                pair = new Pair<>("subject", subject);
                params.add(pair);
                pair = new Pair<>("exam", exam);
                params.add(pair);

                String postString = Utils.createPostString(params);
                Log.i("param", postString);
                MarklistTask marklistTask = new MarklistTask();
                marklistTask.execute(MarklistServerUrl, postString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Server connectivity issue..", Toast.LENGTH_SHORT).show();

        }

    }


    @SuppressLint("StaticFieldLeak")
    public class MarklistTask extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getContext());
            pdia.setMessage("please wait..");
            pdia.setCancelable(true);
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
                    //markListAdapter();
//                } else {
//                    nodata.setVisibility(View.VISIBLE);
//                    scrollDynamic.setVisibility(View.INVISIBLE);
                }
            }
        }//close onPostExecute

        @Override
        public void onCancel(DialogInterface dialogInterface) {
        }
    }

    private void markListAdapter() {
        nodata.setVisibility(View.INVISIBLE);
        scrollDynamic.setVisibility(View.VISIBLE);

        ViewGroup container = root.findViewById(R.id.dynamicView);
        container.removeAllViews();
        TextView examTitile = root.findViewById(R.id.examTitle);
        examTitile.setText(exam);
        for (int i = 0; i < 50; i++) {
            LayoutInflater inflate = LayoutInflater.from(getActivity());
            ViewGroup dynamiclayout = (ViewGroup) inflate.inflate(R.layout.fragment_marklist_container, container, false);

//                if (datajson.get("subject") != null) {
//                    if (datajson.get("subject").equals("Total")) {
//                        TextView count = dynamiclayout.findViewById(R.id.count);
//                        count.setBackgroundColor(getResources().getColor(R.color.black));
//
//                        TextView subject = dynamiclayout.findViewById(R.id.subject);
//                        if (datajson.get("subject") != null) {
//                            subject.setText(datajson.get("subject").toString());
//                            subject.setBackgroundColor(getResources().getColor(R.color.black));
//                            subject.setTextColor(getResources().getColor(R.color.white));
//                        }
//                        // dynamiclayout.addView(subject);
//
//                        TextView mark = dynamiclayout.findViewById(R.id.marks);
//                        if (datajson.get("mark") != null) {
//                            mark.setText(datajson.get("mark").toString());
//                            mark.setBackgroundColor(getResources().getColor(R.color.black));
//                            mark.setTextColor(getResources().getColor(R.color.white));
//
//                        }
//                        // dynamiclayout.addView(mark);
//
//                        TextView grade = dynamiclayout.findViewById(R.id.grade);
//                        if (datajson.get("grade") != null) {
//                            grade.setText(datajson.get("grade").toString());
//                            grade.setBackgroundColor(getResources().getColor(R.color.black));
//                            grade.setTextColor(getResources().getColor(R.color.white));
//
//                        }
//                    } else {

            TextView count = dynamiclayout.findViewById(R.id.count);
            count.setText(String.valueOf(i + 1));
            Random rnd = new Random();
            int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            count.setBackgroundColor(currentColor);

            TextView studentname = dynamiclayout.findViewById(R.id.studentname);
            //  if (datajson.get("subject") != null) {
            studentname.setText("Student " + i);
            // }
            // dynamiclayout.addView(subject);

            TextView mark = dynamiclayout.findViewById(R.id.marks);
            // if (datajson.get("mark") != null) {

            int low = 70;
            int high = 100;
            int result = rnd.nextInt(high - low) + low;
            mark.setText(String.valueOf(result));
            // }
            // dynamiclayout.addView(mark);

            TextView grade = dynamiclayout.findViewById(R.id.grade);
            // if (datajson.get("grade") != null) {
            char c = (char) (rnd.nextInt(5) + 'A');
            grade.setText(String.valueOf(c));
            container.addView(dynamiclayout);
        }

        // dynamiclayout.addView(grade);
    }


    private void getExamDetails(String std, String subject) {
        boolean is_connected = false;
        if (getActivity() != null) {
            if (isNetworkAvailable()) {
                String urls = ServerUtils.ServerUrl;
                String FetchExamServerUrl = urls + ServerUtils.FetchExamsApi;
                Log.d("MarksEntry url", FetchExamServerUrl);
                try {
                    ArrayList<Pair> params = new ArrayList<>();
                    Pair<String, String> pair = new Pair<>("operation", "getexams");
                    params.add(pair);
                    pair = new Pair<>("standard", std);
                    params.add(pair);
                    pair = new Pair<>("subject", subject);
                    params.add(pair);

                    String postString = Utils.createPostString(params);
                    Log.i("param", postString);

                    FetchExamsTask fetchExamsTask = new FetchExamsTask();
                    fetchExamsTask.execute(FetchExamServerUrl, postString);

                    //Log.d("subject_details","--"+ PreferenceUtil.subjectDetailsArray);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_statut", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_stats", "" + e.getMessage());
                }
            }
        }
        return false;
    }

    private class FetchExamsTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getContext());
            pdia.setMessage("please wait..");
            pdia.setCancelable(true);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String res = null;
            try {
                res = CustomHttpClient.executeHttpPost(url[0], url[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            res = "{\"status\":\"true\",\"message\":\"success\",\n" +
                    "\"exams\":[{\"name\":\"First Exam\"},\n" +
                    "{\"name\":\"Second Exam\"}]\n" +
                    "}";
            return res;
        }

        @Override
        protected void onPostExecute(String response) {
            if (pdia != null && pdia.isShowing()) {
                pdia.dismiss();
            }
            if (response != null && !response.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.get("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("exams");
                        String[] exams = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject examsObj = jsonArray.getJSONObject(i);
                            exams[i] = examsObj.get("name").toString();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, exams);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        exam_spinner.setAdapter(adapter);
                        exam = exam_spinner.getSelectedItem().toString();
                    } else {
                        Utils.showAlertMessage(getActivity(), "Exam details currently unavailable.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void dismissProgressDialog() {
        if (pdia != null && pdia.isShowing()) {
            pdia.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        //isDestroyed = true;
        dismissProgressDialog();
        super.onDestroy();
    }

}