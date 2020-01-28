package com.sieva.staffapp.pojoclass;

public class SubjectPOJO {
    private String
            subjectName, subjectId, classID,
            subjectClassName, subjectClassDivision;

    public SubjectPOJO() {
        subjectName = null;
        subjectId = null;
        classID = null;
        subjectClassName = null;
        subjectClassDivision = null;

    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getSubjectClassName() {
        return subjectClassName;
    }

    public void setSubjectClassName(String subjectClassName) {
        this.subjectClassName = subjectClassName;
    }

    public String getSubjectClassDivision() {
        return subjectClassDivision;
    }

    public void setSubjectClassDivision(String subjectClassDivision) {
        this.subjectClassDivision = subjectClassDivision;
    }

}
