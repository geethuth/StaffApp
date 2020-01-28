package com.sieva.staffapp.pojoclass;

public class StaffPOJO {
    private String
            staffName, staffId, schoolID,
            schoolName, designation, email, phoneNo;
//            , geofence;
//    private LatLng
//            boardingPointLatLng;

    public StaffPOJO() {
        staffName = null;
        staffId = null;
        schoolID = null;
        schoolName = null;
        designation = null;
        email = null;
        phoneNo = null;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }


    public String setSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
