package com.sieva.staffapp.helper;

import com.sieva.staffapp.pojoclass.StudentListPOJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentListDetailsHelper {

    public static ArrayList<StudentListPOJO> getStudentList(String studentArrayString) {
        ArrayList<StudentListPOJO>
                sudentListArray = new ArrayList<>();
        try {
            JSONArray studentListDetails;
            if (studentArrayString != null) {
                studentListDetails = new JSONArray(studentArrayString);
                for (int i = 0; i < studentListDetails.length(); i++) {
                    JSONObject studentobj = studentListDetails.getJSONObject(i);
                    StudentListPOJO studentListPOJO = new StudentListPOJO();
                    studentListPOJO.setStudentName(studentobj.get("name").toString());
                    studentListPOJO.setStudentId(studentobj.get("id").toString());
                    sudentListArray.add(studentListPOJO);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sudentListArray;
    }

}
