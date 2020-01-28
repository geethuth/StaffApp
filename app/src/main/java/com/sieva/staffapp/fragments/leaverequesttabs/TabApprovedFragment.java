package com.sieva.staffapp.fragments.leaverequesttabs;


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
import com.sieva.staffapp.adapter.LeaveRequests_ApprovedAdapter;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TabApprovedFragment extends Fragment {

    private RecyclerView
            recyclerView;
    TextView noData;
    private ProgressDialog
            pdia=null;

    public TabApprovedFragment() {
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

        View approvedview = inflater.inflate(
                R.layout.tab_approvedfragment,
                container,
                false);

        /**********************************************************************************************/

        recyclerView = approvedview.findViewById(R.id.leave_list);
        noData = approvedview.findViewById(R.id.no_data);

        /**********************************************************************************************/


        if (getActivity() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        approvedRequestAPI();

        return approvedview;
    }


    private void setStudentDetailsToAdapter(JSONArray detailsArray) {
        LeaveRequests_ApprovedAdapter
                leaveRequestsAdapter;
        if (detailsArray != null) {
            if (detailsArray.length() > 0 && getActivity() != null) {
                leaveRequestsAdapter = new LeaveRequests_ApprovedAdapter(getActivity(), detailsArray);
                recyclerView.setAdapter(leaveRequestsAdapter);
            }
        }
    }


    private void approvedRequestAPI() {

        String
                urls = ServerUtils.ServerUrl,
                approvedServerUrl = ServerUtils.FetchleaverequestsApi,
                approvedUrl = urls + approvedServerUrl;// Serverfilenames.loginUrl;

        Log.d("timeUrl:", approvedUrl);
        try {
            ArrayList<Pair>
                    params = new ArrayList<>();
            // "operation::getleaveletters###classid::1###status::0"
            Pair<String, String>
                    pair = new Pair<>("operation", "getleaveletters");
            params.add(pair);
            pair = new Pair<>("classid", PreferenceUtil.classDetailsArray.get(0).getClassId());
            params.add(pair);
            pair = new Pair<>("status", "1");
            params.add(pair);
            String
                    postString = Utils.createPostString(params);
            Log.i("param", postString);
            System.out.println("approved api: " + postString);
            ApprovedTask
                    approvedTask = new ApprovedTask();
            approvedTask.execute(approvedUrl, postString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ApprovedTask extends AsyncTask<String, Void, String>
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
            System.out.println("Approved response: " + result);
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
                            System.out.println("approved result:" + result);
                            JSONArray
                                    detailsArray = respjson.getJSONArray("data");
                            System.out.println("approved length:" + (detailsArray.length()));
                            Log.d(" approved length:", String.valueOf(detailsArray.length()));

                            if (detailsArray.length() > 0) {
                                setStudentDetailsToAdapter(detailsArray);
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

}
