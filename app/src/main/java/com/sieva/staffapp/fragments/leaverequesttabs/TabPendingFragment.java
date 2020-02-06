package com.sieva.staffapp.fragments.leaverequesttabs;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.sieva.staffapp.adapter.LeaveRequests_PendingAdapter;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TabPendingFragment extends Fragment {

    private static RecyclerView
            recyclerView;
    static TextView noData;
    static ProgressDialog pdia;
    static Context context;

    public TabPendingFragment() {
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

        View pendingview = inflater.inflate(
                R.layout.tab_pendingfragment,
                container,
                false);

        /**********************************************************************************************/

        recyclerView = pendingview.findViewById(R.id.leave_list);
        noData = pendingview.findViewById(R.id.no_data);

        /**********************************************************************************************/

        context = getContext();
        if (getActivity() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        //setStudentDetailsToAdapter();
        if (PreferenceUtil.classDetailsArray != null) {
            pendingRequestAPI();
        } else {
            Utils.showAlertMessage(getActivity(), "Class details is currently unavailable");
        }
        //teachingStaffAPI();

//        }
//        /**********************************************************************************************/


        return pendingview;
    }


    private static void setStudentDetailsToAdapter(JSONArray detailsArray) {
        LeaveRequests_PendingAdapter
                leaveRequestsAdapter;
        if (detailsArray != null) {
            if (detailsArray.length() > 0 && context != null) {
                leaveRequestsAdapter = new LeaveRequests_PendingAdapter(context, detailsArray);
                recyclerView.setAdapter(leaveRequestsAdapter);
            }
        }
    }


    public static void pendingRequestAPI() {
        noData.setVisibility(View.INVISIBLE);

        String
                urls = ServerUtils.ServerUrl,
                pendingServerUrl = ServerUtils.FetchleaverequestsApi,
                pendingUrl = urls + pendingServerUrl;// Serverfilenames.loginUrl;

        Log.d("timeUrl:", pendingUrl);
        try {
            ArrayList<Pair>
                    params = new ArrayList<>();
            // "operation::getleaveletters###classid::1###status::0"
            Pair<String, String>
                    pair = new Pair<>("operation", "getleaveletters");
            params.add(pair);
            pair = new Pair<>("classid", PreferenceUtil.classDetailsArray.get(0).getClassId());
            // pair = new Pair<>("classid", "2");
            params.add(pair);
            pair = new Pair<>("status", "0");
            params.add(pair);
            String
                    postString = Utils.createPostString(params);
//            String
//                    postString = "operation::timetable###schoolid::2098###classname::10B###day::Thursday";
            Log.i("param", postString);
            System.out.println("pending api: " + postString);
            PendingTask
                    pendingTask = new PendingTask();
            pendingTask.execute(pendingUrl, postString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public static class PendingTask extends AsyncTask<String, Void, String>
            implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (context != null) {
                pdia = new ProgressDialog(context);
                pdia.setMessage("Fetching");
                pdia.setCancelable(true);
                pdia.show();
            }
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
            System.out.println("Pending response: " + result);
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
                            System.out.println("pending result:" + result);
                            JSONArray
                                    detailsArray = respjson.getJSONArray("data");
                            System.out.println("pending length:" + (detailsArray.length()));
                            Log.d(" pending length:", String.valueOf(detailsArray.length()));

                            if (detailsArray.length() > 0) {
                                setStudentDetailsToAdapter(detailsArray);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (context != null) {
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

}
