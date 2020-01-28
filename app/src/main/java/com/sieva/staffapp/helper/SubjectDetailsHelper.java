package com.sieva.staffapp.helper;

import com.sieva.staffapp.pojoclass.SubjectPOJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubjectDetailsHelper {

    public static ArrayList<SubjectPOJO> getSubjectList(String subjectArrayString) {
        ArrayList<SubjectPOJO>
                subjectDetailsArray = new ArrayList<>();
        try {
            JSONArray subjectDetails;
            if (subjectArrayString != null) {
                subjectDetails = new JSONArray(subjectArrayString);
                for (int i = 0; i < subjectDetails.length(); i++) {
                    JSONObject subjectobj = subjectDetails.getJSONObject(i);
                    SubjectPOJO subjectPOJO = new SubjectPOJO();
                    subjectPOJO.setSubjectName(subjectobj.get("subjectname").toString());
                    subjectPOJO.setSubjectId(subjectobj.get("subjectid").toString());
                    subjectPOJO.setClassID(subjectobj.get("classid").toString());
                    subjectPOJO.setSubjectClassName(subjectobj.get("class").toString());
                    subjectPOJO.setSubjectClassDivision(subjectobj.getString("division"));
                    subjectDetailsArray.add(subjectPOJO);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subjectDetailsArray;
    }

}
