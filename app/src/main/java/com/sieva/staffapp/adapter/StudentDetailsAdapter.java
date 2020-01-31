package com.sieva.staffapp.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
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
import static com.sieva.staffapp.fragments.AttendanceFragment.removeitem;

public class StudentDetailsAdapter extends RecyclerView.Adapter<StudentDetailsAdapter.ViewHolder> {


    private ArrayList<StudentListPOJO>
            mData;
    private Context
            mContext;
    int itemCount;
    int newPos;
    boolean checked = true;
    private final SparseBooleanArray array = new SparseBooleanArray();

    public StudentDetailsAdapter(Context context, ArrayList<StudentListPOJO> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public StudentDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // checked=true;
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.student_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentDetailsAdapter.ViewHolder holder, int position) {
        // holder.myToggleButton.setChecked(true);

        StudentListPOJO
                studentListPOJO = mData.get(position);
        if (studentListPOJO.getStudentName() != null) {
            holder.studentName.setText(studentListPOJO.getStudentName());
        }
        if (studentListPOJO.getStudentId() != null) {
            holder.studentId.setText("ID: " + studentListPOJO.getStudentId());
        }
//        if (!checked) {
//            newPos = holder.getAdapterPosition();
//            additem(newPos, String.valueOf(checked));
//            holder.myToggleButton.setChecked(checked);
//        }

        holder.myToggleButton.setOnClickListener(arg0 -> {
            newPos = holder.getAdapterPosition();
            System.out.println("position: " + newPos);
            checked = holder.myToggleButton.isChecked();
            holder.myToggleButton.setChecked(holder.myToggleButton.isChecked());
            if (!checked) {
                additem(newPos);
            } else {
                removeitem(newPos);
            }
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
            myToggleButton.setOnClickListener(v -> {
                checked = myToggleButton.isChecked();
                System.out.println("getAdapterPosition(): " + getAdapterPosition());
                array.put(getAdapterPosition(), checked);
                // notifyDataSetChanged();
            });

            // boolean mBool = true;
            //myToggleButton.setChecked(mBool);
//            myToggleButton.setOnClickListener(v -> {
//                myToggleButton.setChecked(false);
//                int pro = getAdapterPosition();
//                System.out.println("position:" + pro);
//            });
        }
    }

}