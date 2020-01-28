package com.sieva.staffapp.helper;

import com.sieva.staffapp.pojoclass.StaffPOJO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StaffDetailsHelper {

    public static ArrayList<StaffPOJO> getStaffList(String staffArrayString) {
        ArrayList<StaffPOJO>
                staffDetailsArray = new ArrayList<>();
        try {
            JSONObject staffDetails;
            if (staffArrayString != null) {
                staffDetails = new JSONObject(staffArrayString);
                staffDetailsArray = new ArrayList<>();
                StaffPOJO staffPOJO = new StaffPOJO();
                staffPOJO.setStaffName(staffDetails.get("staffname").toString());
                staffPOJO.setStaffId(staffDetails.get("staffid").toString());
                staffPOJO.setSchoolID(staffDetails.get("schoolid").toString());
                staffPOJO.setSchoolName(staffDetails.get("schoolname").toString());
                staffPOJO.setDesignation(staffDetails.getString("designation"));
                staffPOJO.setEmail(staffDetails.getString("email"));
                staffPOJO.setPhoneNo(staffDetails.getString("phoneno"));

                staffDetailsArray.add(staffPOJO);

                //staffName, staffId, staffClass, schoolID,
                //            schoolName, designation, email, phoneNo;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return staffDetailsArray;
    }

}
