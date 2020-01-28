package com.sieva.staffapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sieva.staffapp.R;
import com.sieva.staffapp.pojoclass.StudentListPOJO;

import java.util.ArrayList;

import static com.sieva.staffapp.fragments.AttendanceFragment.additem;

public class StudentDetailsAdapter extends RecyclerView.Adapter<StudentDetailsAdapter.ViewHolder> {


    private ArrayList<StudentListPOJO>
            mData;
    private Context
            mContext;
    int itemCount;
    int newPos;

    public StudentDetailsAdapter(Context context, ArrayList<StudentListPOJO> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public StudentDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.student_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentDetailsAdapter.ViewHolder holder, int position) {

        StudentListPOJO
                studentListPOJO = mData.get(position);
        if (studentListPOJO.getStudentName() != null) {
            holder.studentName.setText(studentListPOJO.getStudentName());
        }
        if (studentListPOJO.getStudentId() != null) {
            holder.studentId.setText("ID: " + studentListPOJO.getStudentId());
        }
        holder.myToggleButton.setOnClickListener(arg0 -> {
            newPos = holder.getAdapterPosition();
            System.out.println("position: " + newPos);
            additem(newPos, String.valueOf(holder.myToggleButton.isChecked()));
        });
    }


    @Override
    public int getItemCount() {
        itemCount = mData.size();
        return itemCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                studentName, studentId;
        Switch myToggleButton;

        ViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            studentId = itemView.findViewById((R.id.studentID));
            myToggleButton = itemView.findViewById(R.id.attendanceEntry);
           // boolean mBool = true;
            //myToggleButton.setChecked(mBool);
        }
    }

}