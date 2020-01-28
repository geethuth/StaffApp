package com.sieva.staffapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sieva.staffapp.R;
import com.sieva.staffapp.pojoclass.SubjectPOJO;

import java.util.ArrayList;

public class SubjectDetailsAdapter extends RecyclerView.Adapter<SubjectDetailsAdapter.ViewHolder> {

    private ArrayList<SubjectPOJO>
            mData;
    private Context
            mContext;
    private LinearLayout
            liveTrackLayout, trackHistoryLayout;

    public SubjectDetailsAdapter(Context context, ArrayList<SubjectPOJO> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public SubjectDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.subject_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectDetailsAdapter.ViewHolder holder, int position) {
        SubjectPOJO
                subjectPOJO = mData.get(position);
        if (subjectPOJO.getSubjectName() != null) {
            holder.subjectName.setText("Subject: "+subjectPOJO.getSubjectName());
        } else {
            Toast.makeText(mContext, "Subject list is not available", Toast.LENGTH_SHORT).show();
            // Utils.showAlertMessage(mContext, "Student list is empty");
        }
        if (subjectPOJO.getSubjectClassDivision() != null) {
            holder.divisionName.setText("Division: "+subjectPOJO.getSubjectClassDivision());
        } else {
            Toast.makeText(mContext, "Division list is not available", Toast.LENGTH_SHORT).show();
            // Utils.showAlertMessage(mContext, "Student list is empty");
        }
        if (subjectPOJO.getSubjectClassName() != null) {
            holder.className.setText(subjectPOJO.getSubjectClassName());
        } else {
            Toast.makeText(mContext, "Class list is not available", Toast.LENGTH_SHORT).show();
            // Utils.showAlertMessage(mContext, "Student list is empty");
        }

//        if (subjectPOJO.getSchoolBusNo() != null) {
//            holder.busNumber.setText("Bus Number: " + subjectPOJO.getSchoolBusNo());
//        } else {
//            //Utils.showAlertMessage(mContext, "Bus is not selected");
//        }

//        liveTrackLayout.setOnClickListener(clickedView -> {
//            ArrayList<String>
//                    tempStudentList = new ArrayList<>();
//            if (PreferenceUtil.studentDetailsArray != null) {
//                tempStudentList.add(PreferenceUtil.studentDetailsArray.get(position).getStudentName());
//                Bundle
//                        bundle = new Bundle();
//                Intent
//                        intent = new Intent(mContext, MainActivity.class);
//                bundle.putString("student_livetrack", tempStudentList.get(0));
//                System.out.println("live clicked");
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
//            }
//        });
//
//        trackHistoryLayout.setOnClickListener(clickedView -> {
//            if (PreferenceUtil.studentDetailsArray != null) {
//                ArrayList<String>
//                        tempStudentList = new ArrayList<>();
//                tempStudentList.add(PreferenceUtil.studentDetailsArray.get(position).getStudentName());
//                Bundle
//                        bundle = new Bundle();
//                Intent
//                        intent = new Intent(mContext, MainActivity.class);
//                bundle.putString("student_trackhistory", tempStudentList.get(0));
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                className, subjectName, divisionName;

        ViewHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
            subjectName = itemView.findViewById((R.id.subjectName));
            divisionName = itemView.findViewById((R.id.divisionName));
        }

    }

}