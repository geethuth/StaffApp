package com.sieva.staffapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sieva.staffapp.R;
import com.sieva.staffapp.pojoclass.StudentListPOJO;
import com.sieva.staffapp.util.PreferenceUtil;

import java.util.ArrayList;

public class StudentDetailsAdapter1 extends RecyclerView.Adapter<StudentDetailsAdapter1.ViewHolder> {

    private ArrayList<StudentListPOJO>
            mData;
    private Context
            mContext;
    private LinearLayout
            liveTrackLayout, trackHistoryLayout;
    Switch myToggleButton;
    public static String[] attendance;
    int itemCount;

    public StudentDetailsAdapter1(Context context, ArrayList<StudentListPOJO> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public StudentDetailsAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.student_details_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull StudentDetailsAdapter1.ViewHolder holder, int position) {
        StudentListPOJO
                studentListPOJO = mData.get(position);
        if (studentListPOJO.getStudentName() != null) {
            holder.studentName.setText(studentListPOJO.getStudentName());
        }
        if (studentListPOJO.getStudentId() != null) {
            holder.studentId.setText("ID: " + studentListPOJO.getStudentId());
        }
        attendance[position] = String.valueOf(myToggleButton.isChecked());
        System.out.println("present: " + String.valueOf(myToggleButton.isChecked()));
        myToggleButton.setOnCheckedChangeListener((toggleButton, isChecked) -> {
            if (isChecked) {
                attendance[position] = String.valueOf(myToggleButton.isChecked());
                System.out.println("present: ");

            }
            // Do something
            else {
                attendance[position] = String.valueOf(myToggleButton.isChecked());
                System.out.println("absent: ");

            }
        });
        System.out.println("position: " + position);
        System.out.println("PreferenceUtil.StudentListArray.size(): " + PreferenceUtil.StudentListArray.size());
        System.out.println("attendance size: " + attendance.length);

        if (position == PreferenceUtil.StudentListArray.size()-1) {
            System.out.println("attendance value: " + attendance.length);
        }

    }

    @Override
    public int getItemCount() {
        itemCount = mData.size();
        attendance = new String[itemCount];
        return itemCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                studentName, studentId;

        ViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            studentId = itemView.findViewById((R.id.studentID));
            myToggleButton = itemView.findViewById(R.id.attendanceEntry);
        }
    }

}