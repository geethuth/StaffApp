package com.sieva.staffapp.fragments.timetabletabs;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sieva.staffapp.R;
import com.sieva.staffapp.adapter.TimeTableAdapter;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TuesdayFragment extends Fragment {

    private RecyclerView
            recyclerView;
    TextView noData;
    private ProgressDialog
            pdia = null;

    public TuesdayFragment() {
        // Required empty public constructor
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

        View timetableView = inflater.inflate(
                R.layout.tab_timetablefragment_wednesday,
                container,
                false);

        /**********************************************************************************************/

        recyclerView = timetableView.findViewById(R.id.period_list);
        noData = timetableView.findViewById(R.id.no_data);

        /**********************************************************************************************/


        if (getActivity() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        timetableAPI();

        return timetableView;
    }


    private void setPeriodsDetailsToAdapter(JSONArray detailsArray) {
        TimeTableAdapter
                timeTableAdapter;
        if (detailsArray != null) {
            if (detailsArray.length() > 0 && getActivity() != null) {
                timeTableAdapter = new TimeTableAdapter(getActivity(), detailsArray, "Tuesday");
                recyclerView.setAdapter(timeTableAdapter);
            }
        }
    }


    private void timetableAPI() {

        String
                urls = ServerUtils.ServerUrl,
                timetableServerUrl = ServerUtils.TimetableApi,
                timetableUrl = urls + timetableServerUrl;// Serverfilenames.loginUrl;

        Log.d("timeUrl:", timetableUrl);
        try {
            ArrayList<Pair>
                    params = new ArrayList<>();
            //  "operation::stafftimetable###staffid::144###day::Thursday";
            Pair<String, String>
                    pair = new Pair<>("operation", "stafftimetable");
            params.add(pair);
            pair = new Pair<>("staffid", PreferenceUtil.staffDetailsArray.get(0).getStaffId());
            params.add(pair);
            pair = new Pair<>("day", "Tuesday");
            params.add(pair);
            String
                    postString = Utils.createPostString(params);
            Log.i("param", postString);
            System.out.println("timetable api: " + postString);
            TimetableTask
                    timetableTask = new TimetableTask();
            timetableTask.execute(timetableUrl, postString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class TimetableTask extends AsyncTask<String, Void, String>
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
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Timetable response: " + result);
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
                    if (result.contains("true")) {
                        // if (!result.contains("null")) {
                        recyclerView.setVisibility(View.VISIBLE);
                        respjson = new JSONObject(result);
                        try {
                            System.out.println("timetable result:" + result);
                            JSONArray
                                    detailsArray = respjson.getJSONArray("data");
                            System.out.println("timetable length:" + (detailsArray.length()));
                            Log.d(" timetable length:", String.valueOf(detailsArray.length()));

                            if (detailsArray.length() > 0) {
                                sortDataOnPeriods(detailsArray);
                                //setPeriodsDetailsToAdapter(detailsArray);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

    private void sortDataOnPeriods(JSONArray detailsArray) throws JSONException {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        for (int i = 0; i < detailsArray.length(); i++) {
            jsonList.add(detailsArray.getJSONObject(i));
        }
        Collections.sort(jsonList, (a, b) -> {
            int valA = 0;
            int valB = 0;

            try {
                valA = Integer.parseInt(a.get("period").toString());
                valB = Integer.parseInt(b.get("period").toString());
                System.out.println("value a: " + valA);
                System.out.println("value b: " + valB);

            } catch (JSONException e) {
                //do something
            }
            int value = Integer.compare(valA, valB);
            System.out.println("value: " + value);
            return value;
        });

        for (int i = 0; i < detailsArray.length(); i++) {
            sortedJsonArray.put(jsonList.get(i));
        }
        System.out.println("sorted: " + sortedJsonArray.toString());
        setPeriodsDetailsToAdapter(sortedJsonArray);
    }

}