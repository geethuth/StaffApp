package com.sieva.staffapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sieva.staffapp.R;
import com.sieva.staffapp.adapter.EventDetailsAdapter;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    FloatingActionButton floatingActionButton;
    ProgressDialog pdia;
    DatePickerDialog datePickerDialog;
    private RecyclerView
            recyclerView;
    TextView noData;
    String className;
    JSONObject jsonObject;
    static EventDetailsAdapter eventDetailsAdapter;
    private String[]
            divisionArray;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View eventView = inflater.inflate(R.layout.fragment_events, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        recyclerView = eventView.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        RelativeLayout
                studentSelect = eventView.findViewById(R.id.student_select_popup);
        Spinner studentSpinner = eventView.findViewById(R.id.studentSpinner);

        if (PreferenceUtil.subjectDetailsArray != null) {
            //studentName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            divisionArray = new String[PreferenceUtil.subjectDetailsArray.size()];

            if (!PreferenceUtil.subjectDetailsArray.isEmpty()) {
                for (int i = 0; i < PreferenceUtil.subjectDetailsArray.size(); i++) {
                    divisionArray[i] = PreferenceUtil.subjectDetailsArray.get(i).getSubjectClassName() + "-" + PreferenceUtil.subjectDetailsArray.get(i).getSubjectClassDivision();

                }
            }
        } else if (PreferenceUtil.classDetailsArray != null) {
            divisionArray = new String[PreferenceUtil.classDetailsArray.size()];
            if (!PreferenceUtil.classDetailsArray.isEmpty()) {
                for (int i = 0; i < PreferenceUtil.classDetailsArray.size(); i++) {
                    divisionArray[i] = PreferenceUtil.classDetailsArray.get(i).getClassName() + "-" + PreferenceUtil.classDetailsArray.get(i).getClassDivision();
                }
            }
        }
        ArrayAdapter<String> adapter_student = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                divisionArray);
        if (PreferenceUtil.subjectDetailsArray != null) {
            if (!PreferenceUtil.subjectDetailsArray.isEmpty()) {
                className = PreferenceUtil.subjectDetailsArray.get(0).getSubjectClassName() + "-" + PreferenceUtil.subjectDetailsArray.get(0).getSubjectClassDivision();
            }
        } else if (PreferenceUtil.classDetailsArray != null) {
            if (!PreferenceUtil.classDetailsArray.isEmpty()) {
                className = PreferenceUtil.classDetailsArray.get(0).getClassName() + "-" + PreferenceUtil.classDetailsArray.get(0).getClassDivision();
            }
        }

        adapter_student.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        studentSpinner.setAdapter(adapter_student);
        studentSpinner.setOnItemSelectedListener(EventsFragment.this);

        if (className != null) {
            int spinnerPosition = adapter_student.getPosition(className);
            studentSpinner.setSelection(spinnerPosition);
        }

        studentSelect.setOnClickListener(v -> studentSpinner.setOnItemSelectedListener(this));
        noData = eventView.findViewById(R.id.noData);
        //final TextView textView = eventView.findViewById(R.id.text_notifications);
        final TextView toolbarmsg = eventView.findViewById(R.id.toolbar_msg);
        toolbarmsg.setText("Events");
        floatingActionButton = eventView.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(v -> {
            LayoutInflater inflater1 = getLayoutInflater();
            View alertLayout = inflater1.inflate(R.layout.custom_event_dialog, null);
            TextView customTitleView = new TextView(getContext());
            customTitleView.setText("\tNew Events\t");
            customTitleView.setPadding(20, 30, 20, 30);
            customTitleView.setTextSize(20F);
            customTitleView.setBackgroundColor(getResources().getColor(R.color.gradStop));
            customTitleView.setTextColor(Color.WHITE);

            final TextView startingDate = (TextView) alertLayout.findViewById(R.id.startDate_text);

            startingDate.setOnClickListener(new View.OnClickListener() {
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
                    datePickerDialog = new DatePickerDialog(getContext(), R.style.datepicker, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                        Log.d("Selected Date", selectedDay + " " + selectedMonth + " " + selectedYear);
//                        Date date = new Date();
//                        Calendar cal = Calendar.getInstance();
//                        cal.set(selectedYear, selectedMonth, selectedDay);
//                        date.setTime(cal.getTime().getTime());
//                        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format));
//                        dt = dateFormat.format(date);
                        startingDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                        // fuel_values.set(0,SD.getText());
                        startdate = startingDate.getText().toString();
                        Log.d("Selected start Date", startdate);
                        System.out.println("startdate " + startdate);

                    }, year, month, day);
                    // Set Max date and Min date for add date
                    // datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                    // datePickerDialog.setTitle("Pick start date");
                    datePickerDialog.show();
                }
            });


            // End Date
            Date date = new Date();
            final SimpleDateFormat[] curFormater = {new SimpleDateFormat(getString(R.string.date_format))};
            String startDate = curFormater[0].format(date);
            Log.d("end date", startDate);
            startingDate.setText(startDate);

            final EditText event_Desc = alertLayout.findViewById(R.id.leave_desc);
            ImageView send = alertLayout.findViewById(R.id.send);
            ImageView delete = alertLayout.findViewById(R.id.delete);
            EditText title_text = alertLayout.findViewById(R.id.title_text);
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setCustomTitle(customTitleView);

            // alert.setTitle("\tLeave Request\t");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // Allow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            AlertDialog dialog = alert.create();
            delete.setOnClickListener(v1 -> {
                title_text.getText().clear();
                event_Desc.getText().clear();
            });
            send.setOnClickListener(v1 -> {
                if (title_text.getText() != null && event_Desc.getText() != null && startingDate.getText() != null) {
                    if (event_Desc.getText().toString().length() > 0 && title_text.getText().toString().length() > 0) {
                        String
                                urls = ServerUtils.ServerUrl,
                                eventSubmissionServerUrl = ServerUtils.SubmitClassAlertsApi,
                                eventSubmissionServerUrlUrl = urls + eventSubmissionServerUrl;// Serverfilenames.loginUrl;
//$payload = "operation::submitclassalert###classid::1###eventdate::13/01/2020
// ###title::Class title###content::Class alert for 13 Jan 2020";
                        Log.d("eventSubmissionUrl:", eventSubmissionServerUrlUrl);
                        try {
                            ArrayList<Pair>
                                    params = new ArrayList<>();
                            Pair<String, String>
                                    pair = new Pair<>("operation", "submitclassalert");
                            params.add(pair);
                            pair = new Pair<>("classid", PreferenceUtil.classDetailsArray.get(0).getClassId());
                            params.add(pair);
                            pair = new Pair<>("eventdate", startingDate.getText().toString());
                            params.add(pair);
                            pair = new Pair<>("title", title_text.getText().toString());
                            params.add(pair);
                            pair = new Pair<>("content", event_Desc.getText().toString());
                            params.add(pair);
                            String
                                    postString = Utils.createPostString(params);
                            Log.i("param", postString);
                            System.out.println("events alert api: " + postString);
                            EventSubmissionServerUrlTask
                                    eventSubmissionServerUrlTask = new EventSubmissionServerUrlTask();
                            eventSubmissionServerUrlTask.execute(eventSubmissionServerUrlUrl, postString);
                            if (dialog.isShowing()) {
                                dialog.cancel();
                            }

                            jsonObject = new JSONObject();
                            jsonObject.put("eventdate", startingDate.getText().toString());
                            jsonObject.put("title", title_text.getText().toString());
                            jsonObject.put("content", event_Desc.getText().toString());
                            Date d = new Date();
                            curFormater[0] = new SimpleDateFormat("yyyy-MM-dd");
                            String currentDate = curFormater[0].format(d);

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                            int currentMinute = cal.get(Calendar.MINUTE);
                            int currentSecond = cal.get(Calendar.MINUTE);
                            System.out.println("minute hr: " + currentHour + "\t" + currentMinute);
                            jsonObject.put("submittedon", currentDate + " " + currentHour + ":" + currentMinute + ":" + currentSecond);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            dialog.show();
        });

        return eventView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        displayAlertsAPI();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @SuppressLint("StaticFieldLeak")
    public class EventSubmissionServerUrlTask extends AsyncTask<String, Void, String>
            implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getContext());
            pdia.setMessage("Fetching");
            pdia.setCancelable(true);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String
                    res = null, response;
            try {
                response = CustomHttpClient.executeHttpPost(url[0], url[1]);
                res = response;
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("EventsAlert response: " + res);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("EventsAlert response: " + result);
            if (pdia != null
                    && pdia.isShowing()) {
                pdia.dismiss();
                pdia = null;
            }
            if (result != null
                    && !result.equals("")) {
                Log.d("result", result);
                try {
                    if (result.contains("success")) {
                        // if (!result.contains("null")) {
                        recyclerView.setVisibility(View.VISIBLE);
                        if (jsonObject.length() > 0) {
                            Utils.showAlertMessage(getActivity(), "Events added successfully");
                            updateEventAdapter(jsonObject);
                        }

                    } else {
                        if (getActivity() != null) {
                            recyclerView.setVisibility(View.INVISIBLE);
                            noData.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
        }
    }

    private void updateEventAdapter(JSONObject detailsObject) {
        noData.setVisibility(View.GONE);
        EventDetailsAdapter.updateAdapter(detailsObject);
        eventDetailsAdapter.notifyDataSetChanged();
    }


    private void displayAlertsAPI() {
        String
                urls = ServerUtils.ServerUrl,
                alertServerUrl = ServerUtils.FetchClassAlertsApi,
                alertServerUrlUrl = urls + alertServerUrl;// Serverfilenames.loginUrl;

        Log.d("eventUrl:", alertServerUrlUrl);
        try {
            ArrayList<Pair>
                    params = new ArrayList<>();
            Pair<String, String>
                    pair = new Pair<>("operation", "classalerts");
            params.add(pair);
            pair = new Pair<>("classid", PreferenceUtil.classDetailsArray.get(0).getClassId());
            params.add(pair);
            String
                    postString = Utils.createPostString(params);
            Log.i("param", postString);
            System.out.println("events alert api: " + postString);
            EventsAlertServerUrlTask
                    eventsAlertServerUrlTask = new EventsAlertServerUrlTask();
            eventsAlertServerUrlTask.execute(alertServerUrlUrl, postString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class EventsAlertServerUrlTask extends AsyncTask<String, Void, String>
            implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getContext());
            pdia.setMessage("Fetching");
            pdia.setCancelable(true);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String
                    res = null, response;
            try {
                response = CustomHttpClient.executeHttpPost(url[0], url[1]);
                res = response;
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("EventsAlert response: " + res);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("EventsAlert response: " + result);
            if (pdia != null
                    && pdia.isShowing()) {
                pdia.dismiss();
                pdia = null;
            }
            if (result != null
                    && !result.equals("")) {
                Log.d("result", result);
                try {
                    JSONObject respjson;
                    if (result.contains("success")) {
                        // if (!result.contains("null")) {
                        recyclerView.setVisibility(View.VISIBLE);
                        respjson = new JSONObject(result);
                        try {
                            JSONArray
                                    detailsArray = respjson.getJSONArray("data");

                            if (detailsArray.length() > 0) {
                                setEventsDetailsToAdapter(detailsArray);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        clearAlldata();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
        }
    }

    private void clearAlldata() {
        eventDetailsAdapter = new EventDetailsAdapter(getContext(), new JSONArray());
        recyclerView.setAdapter(eventDetailsAdapter);
        noData.setVisibility(View.VISIBLE);
    }

    private void setEventsDetailsToAdapter(JSONArray detailsArray) {
        if (noData != null) {
            noData.setVisibility(View.GONE);
            eventDetailsAdapter = new EventDetailsAdapter(getContext(), detailsArray);
            recyclerView.setAdapter(eventDetailsAdapter);
        }
    }
}