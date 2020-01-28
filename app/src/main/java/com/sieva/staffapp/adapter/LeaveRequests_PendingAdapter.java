package com.sieva.staffapp.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulhakeem.seemoretextview.SeeMoreTextView;
import com.sieva.staffapp.R;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaveRequests_PendingAdapter extends RecyclerView.Adapter<LeaveRequests_PendingAdapter.ViewHolder> {


    private JSONArray
            mData;
    private Context
            mContext;
    private ProgressDialog
            pdia;
    RelativeLayout buttonLayout;
    int newPosition;

    public LeaveRequests_PendingAdapter(Context context, JSONArray data) {
        this.mContext = context;
        this.mData = data;

    }

    @NonNull
    @Override
    public LeaveRequests_PendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.leaverequest_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveRequests_PendingAdapter.ViewHolder holder, int position) {
        System.out.println("Position: " + position);

        if (mData != null) {
            if (mData.length() > 0) {
                try {
                    JSONObject
                            datajson = mData.getJSONObject(position);
                    if (datajson.get("studentname") != null) {
                        holder.studentName.setText(datajson.get("studentname").toString());
                    }
                    if (datajson.get("submittedon") != null) {
                        String dateString = datajson.get("submittedon").toString().split(" ")[0];
                        System.out.println("datestring:" + dateString);
                        String newDateString = dateString.split("-")[2] + "/" + dateString.split("-")[1] + "/" + dateString.split("-")[0];
                        String timeString = datajson.get("submittedon").toString().split(" ")[1];
                        holder.submitDate.setText(newDateString + " " + timeString);
                    }
                    if (datajson.get("title") != null) {
                        holder.requestTitle.setText(datajson.get("title").toString());
                    }
                    if (datajson.get("content") != null) {
                        holder.requestBody.setTextMaxLength(70);
                        holder.requestBody.setSeeMoreTextColor(R.color.gradStart);
                        holder.requestBody.setSeeMoreText("Read more", "Read less");
                        holder.requestBody.setContent(datajson.get("content").toString());
                    }
                    if (datajson.get("startdate") != null && datajson.get("enddate") != null) {
                        holder.dateRange.setText(datajson.get("startdate").toString() + " - " + datajson.get("enddate").toString());
                    }
                    // holder.leaveStatus.setText("Pending " + " " + datajson.get("requestid").toString());
                    holder.leaveStatus.setText("Pending ");
                    holder.approvaLayout.setOnClickListener(clickedView -> {
                        newPosition = holder.getAdapterPosition();
                        System.out.println("view holder newPosition: " + newPosition);

                        try {
                            if (datajson.get("requestid") != null && datajson.get("enddate") != null) {
                                approveAPI(datajson.get("requestid").toString(), "1");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    });
                    holder.rejectLayout.setOnClickListener(clickedView -> {
                        newPosition = holder.getAdapterPosition();
                        System.out.println("view holder newPosition: " + newPosition);

                        try {
                            if (datajson.get("requestid") != null && datajson.get("enddate") != null) {
                                approveAPI(datajson.get("requestid").toString(), "2");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void approveAPI(String requestid, String status) {
        String
                urls = ServerUtils.ServerUrl,
                approveLeaveReqApi = ServerUtils.ApproveLeaveReqApi,
                approveLeaveReqApiUrl = urls + approveLeaveReqApi;// Serverfilenames.loginUrl;

        Log.d("timeUrl:", approveLeaveReqApiUrl);
        try {
            ArrayList<Pair>
                    params = new ArrayList<>();
            // "operation::approveleave###leaveletterid::7878###status::1"
            Pair<String, String>
                    pair = new Pair<>("operation", "approveleave");
            params.add(pair);
            pair = new Pair<>("leaveletterid", requestid);
            params.add(pair);
            pair = new Pair<>("status", status);
            params.add(pair);
            String
                    postString = Utils.createPostString(params);
//            String
//                    postString = "operation::timetable###schoolid::2098###classname::10B###day::Thursday";
            Log.i("param", postString);
            System.out.println("pending api: " + postString);
            ApprovingTask
                    approvingTask = new ApprovingTask();
            approvingTask.execute(approveLeaveReqApiUrl, postString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class ApprovingTask extends AsyncTask<String, Void, String>
            implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(mContext);
            pdia.setMessage("Submitting..");
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
            if (pdia != null
                    && pdia.isShowing()) {
                pdia.dismiss();
            }
            if (result != null
                    && !result.equals("")) {
                Log.d("result", result);
                if (result.contains("true")) {
                    AlertDialog SuccessAlert = new AlertDialog.Builder(mContext).create();
                    SuccessAlert.setMessage("Leave request status updated successfully");
                    SuccessAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialogInterface, i) -> {
                                removeItem(newPosition);
                            }
                    );
                    SuccessAlert.show();
                } else {
                    AlertDialog
                            alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("Failed..!");
                    alertDialog.setMessage("Please submit correct details.");
                    alertDialog.setButton(
                            androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL,
                            "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                }

            }
        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
        }
    }

    private void removeItem(int newPosition) {
        System.out.println("newPosition: " + newPosition);
        mData.remove(newPosition);
        notifyItemRemoved(newPosition);
        //notifyItemRangeChanged(newPosition, mData.length());
    }

    @Override
    public int getItemCount() {
        return mData.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                studentName, submitDate, requestTitle,
                dateRange, leaveStatus;
        SeeMoreTextView requestBody;

        LinearLayout approvaLayout, rejectLayout;

        ViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            submitDate = itemView.findViewById((R.id.submittedDate));
            requestTitle = itemView.findViewById((R.id.requestTitle));
            requestBody = itemView.findViewById((R.id.requestBody));
            dateRange = itemView.findViewById((R.id.dateRange));
            leaveStatus = itemView.findViewById((R.id.leaveStatus));
            buttonLayout = itemView.findViewById((R.id.buttonLayout));
            approvaLayout = itemView.findViewById((R.id.approvalLayout));
            rejectLayout = itemView.findViewById((R.id.rejectlayout));
        }
    }

}