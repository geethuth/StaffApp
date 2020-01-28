package com.sieva.staffapp.helper;

import com.sieva.staffapp.pojoclass.ClassPOJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClassDetailsHelper {

    public static ArrayList<ClassPOJO> getClassList(String classArrayString) {
        ArrayList<ClassPOJO>
                classDetailsArray = new ArrayList<>();
        try {
            JSONArray classDetails;
            if (classArrayString != null) {
                classDetails = new JSONArray(classArrayString);
                for (int i = 0; i < classDetails.length(); i++) {
                    JSONObject classobj = classDetails.getJSONObject(i);
                    ClassPOJO classPOJO = new ClassPOJO();
                    classPOJO.setClassName(classobj.get("class").toString());
                    classPOJO.setClassId(classobj.get("classid").toString());
                    classPOJO.setClassDivision(classobj.getString("division"));
                    classDetailsArray.add(classPOJO);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return classDetailsArray;
    }

}
