package com.sieva.staffapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sieva.staffapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
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
        if (mData != null) {
            if (mData.length() > 0) {
                try {
                    JSONObject datajson = mData.getJSONObject(position);
                    holder.finalLayout.getBackground().mutate().setTint(currentColor);
                    //holder.finalLayout.getBackground().invalidateSelf();
                    holder.initialLayout.getBackground().mutate().setTint(currentColor);
                    if (datajson.get("time") != null) {
                        String startTime = datajson.get("time").toString().split("-")[0];
//                        int startHour = Integer.parseInt(startTime.split(":")[0]);
//                        int startMinute = Integer.parseInt(startTime.split(":")[1]);

                        String endTime = datajson.get("time").toString().split("-")[1];
//                        int endHour = Integer.parseInt(endTime.split(":")[0]);
//                        int endMinute = Integer.parseInt(endTime.split(":")[1]);

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        Date d = new Date();
                        String dayOfTheWeek = sdf.format(d);
                        System.out.println("dayOfTheWeek: " + dayOfTheWeek);
                        System.out.println("mDay: " + mDay);
                        if (mDay.equals(dayOfTheWeek)) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                            int currentMinute = cal.get(Calendar.MINUTE);
                            System.out.println("minute hr: " + currentHour + "\t" + currentMinute);

                            @SuppressLint("SimpleDateFormat") Date time1 = new SimpleDateFormat("HH:mm").parse(startTime);
                            System.out.println("time1: " + time1);

                            @SuppressLint("SimpleDateFormat") Date time2 = new SimpleDateFormat("HH:mm").parse(endTime);
                            System.out.println("time2 " + time2);

                            @SuppressLint("SimpleDateFormat") Date currentTime = new SimpleDateFormat("HH:mm").parse(currentHour%12 + ":" + currentMinute);
                            System.out.println("currentTime " + currentTime);
                            if (time2.after(currentTime) && time1.before(currentTime)) {
                                Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.timetable_icon);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "my_channel_id_01")
                                        .setLargeIcon(largeIcon)
                                        .setSmallIcon(R.drawable.timetable_icon)
                                        .setContentTitle("You have class now!!")
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(mDay + " " + datajson.get("time").toString() +
                                                        "\nClass:" + datajson.get("class").toString() + " " + datajson.get("division").toString() +
                                                        "\t\tSubject: " + datajson.get("subject").toString()))
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

                                // notificationId is a unique int for each notification that you must define
                                notificationManager.notify(1, builder.build());

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
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public int getItemCount() {
        //return mData.length();
        return mData.length();
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