package com.sieva.staffapp.pojoclass;

public class StudentListPOJO {
    private String
            studentName, studentId;


    public StudentListPOJO() {
        studentName = null;
        studentId = null;
    }


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
