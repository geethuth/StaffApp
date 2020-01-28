package com.sieva.staffapp.util;

import com.sieva.staffapp.pojoclass.ClassPOJO;
import com.sieva.staffapp.pojoclass.StaffPOJO;
import com.sieva.staffapp.pojoclass.StudentListPOJO;
import com.sieva.staffapp.pojoclass.SubjectPOJO;

import java.util.ArrayList;

public class PreferenceUtil {

    public static String
            username = null, password = null, authCode = null;
    public static int flag;

    public static ArrayList<StaffPOJO> staffDetailsArray = null;
    public static ArrayList<ClassPOJO> classDetailsArray = null;
    public static ArrayList<SubjectPOJO> subjectDetailsArray = null;
    public static ArrayList<StudentListPOJO> StudentListArray = null;
}