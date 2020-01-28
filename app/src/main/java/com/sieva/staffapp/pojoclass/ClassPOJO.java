package com.sieva.staffapp.pojoclass;

public class ClassPOJO {
    private String
            className, classId,
            classDivision;


    public ClassPOJO() {
        className = null;
        classId = null;
        classDivision = null;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassDivision() {
        return classDivision;
    }

    public void setClassDivision(String classDivision) {
        this.classDivision = classDivision;
    }

}
