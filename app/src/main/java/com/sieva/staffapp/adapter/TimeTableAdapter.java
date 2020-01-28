package com.sieva.staffapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sieva.staffapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder> {


    private JSONArray
            mData;
    private Context
            mContext;
    private String
            mDay;

    public TimeTableAdapter(Context context, JSONArray data, String day) {
        this.mContext = context;
        this.mData = data;
        this.mDay = day;


    }

    @NonNull
    @Override
    public TimeTableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.periods_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableAdapter.ViewHolder holder, int position) {
        System.out.println("Position: " + position);
        Random rnd = new Random();
        int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.finalLayout.getBackground().setTint(currentColor);
        holder.initialLayout.getBackground().setTint(currentColor);
        if (mData != null) {
            if (mData.length() > 0) {
                try {
                    JSONObject
                            datajson = mData.getJSONObject(position);
                    if (datajson.get("time") != null) {
                        String startTime = datajson.get("time").toString().split("-")[0];
                        int startHour = Integer.parseInt(startTime.split(":")[0]);
                        int startMinute = Integer.parseInt(startTime.split(":")[1]);

                        String endTime = datajson.get("time").toString().split("-")[1];
                        int endHour = Integer.parseInt(endTime.split(":")[0]);
                        int endMinute = Integer.parseInt(endTime.split(":")[1]);

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        Date d = new Date();
                        String dayOfTheWeek = sdf.format(d);
                        System.out.println("dayOfTheWeek: " + dayOfTheWeek);
                        if (mDay.equals(dayOfTheWeek)) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                            int currentMinute = cal.get(Calendar.MINUTE);
                            System.out.println("minute hr: " + currentHour + "\t" + currentMinute);

                            Calendar firstLimit = Calendar.getInstance();
                            firstLimit.set(Calendar.HOUR, startHour);
                            firstLimit.set(Calendar.MINUTE, startMinute);

                            Calendar secondLimit = Calendar.getInstance();
                            secondLimit.set(Calendar.HOUR, endHour);
                            secondLimit.set(Calendar.MINUTE, endMinute);

                            Calendar classTime = Calendar.getInstance();
                            classTime.set(Calendar.HOUR, currentHour % 2);
                            classTime.set(Calendar.MINUTE, currentMinute);

                            if (classTime.after(firstLimit) && classTime.before(secondLimit)) {
                                holder.timeRange.setText("NOW");
                                holder.timeRange.setTextColor(Color.RED);
                            } else {
                                holder.timeRange.setText(datajson.get("time").toString());
                                holder.timeRange.setTextColor(Color.BLACK);
                            }
                        } else {
                            holder.timeRange.setText(datajson.get("time").toString());
                            holder.timeRange.setTextColor(Color.BLACK);
                        }
                    }
                    if (datajson.get("subject") != null) {
                        holder.subject.setText(datajson.get("subject").toString());
                        holder.subject.setTextColor(currentColor);
                    }
                    if (datajson.get("class") != null && datajson.get("division") != null) {
                        holder.className.setText(datajson.get("class").toString() + " " + datajson.get("division").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public int getItemCount() {
        //return mData.length();
        return 3;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                timeRange, subject, className;
        RelativeLayout initialLayout, finalLayout;

        ViewHolder(View itemView) {
            super(itemView);
            timeRange = itemView.findViewById(R.id.timeRange);
            subject = itemView.findViewById((R.id.subject));
            className = itemView.findViewById((R.id.className));
            initialLayout = itemView.findViewById((R.id.initialLyout));
            finalLayout = itemView.findViewById((R.id.finalLyout));
        }
    }
}