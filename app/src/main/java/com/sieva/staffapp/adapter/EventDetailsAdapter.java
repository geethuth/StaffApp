package com.sieva.staffapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sieva.staffapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventDetailsAdapter extends RecyclerView.Adapter<EventDetailsAdapter.ViewHolder> {


    private static JSONArray
            mData;
    private Context
            mContext;
    int itemCount;


    public EventDetailsAdapter(Context context, JSONArray data) {
        this.mContext = context;
        mData = data;
    }

    @NonNull
    @Override
    public EventDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.classalerts_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventDetailsAdapter.ViewHolder holder, int position) {
        if (mData != null) {
            if (mData.length() > 0) {
                try {
                    JSONObject
                            datajson = mData.getJSONObject(position);
                    if (datajson.get("eventdate") != null) {
                        holder.eventdate.setText(datajson.get("eventdate").toString());
                    }
                    if (datajson.get("title") != null) {
                        holder.eventName.setText(datajson.get("title").toString());
                    }
                    if (datajson.get("content") != null) {
                        holder.eventContent.setText(datajson.get("content").toString());
                    }
                    if (datajson.get("submittedon") != null) {
//                        String dateString = datajson.get("submittedon").toString().split(" ")[0];
//                        System.out.println("datestring:" + dateString);
//                        String newDateString = dateString.split("-")[2] + "/" + dateString.split("-")[1] + "/" + dateString.split("-")[0];
//                        String timeString = datajson.get("submittedon").toString().split(" ")[1];
//                        holder.eventSubmitDate.setText("Submitted on: " + newDateString + " " + timeString);
                        holder.eventSubmitDate.setText("Submitted on: " + datajson.get("submittedon").toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public int getItemCount() {
        itemCount = mData.length();
        System.out.println("detailsArray length:" + mData.length());

        return itemCount;
    }

    public static void updateAdapter(JSONObject detailsObject) {
        mData.put(detailsObject);
        //notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                eventName, eventSubmitDate, eventContent, eventdate;

        ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.alert_name);
            eventSubmitDate = itemView.findViewById((R.id.alert_submitDate));
            eventContent = itemView.findViewById(R.id.alert_desc);
            eventdate = itemView.findViewById(R.id.alert_date);
        }
    }

}