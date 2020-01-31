package com.sieva.staffapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.sieva.staffapp.R;

import java.util.ArrayList;

public class Utils {

    public static String createPostString(ArrayList<Pair> params) {
        String postString = "";

        for (int i = 0; i < params.size(); i++) {
            postString += (params.get(i).first + "::" + params.get(i).second);
            if (i < (params.size() - 1)) {
                postString += "###";
            }
        }

        return postString;
    }

    public static String createPostStringTrackhistory(ArrayList<Pair> params) {
        String postString = "";

        for (int i = 0; i < params.size(); i++) {
            postString += (params.get(i).first + "=>" + params.get(i).second);
            if (i < (params.size() - 1)) {
                postString += ",";
            }
        }

        return postString;
    }

    public static void showAlertMessage(Context context, String message) {
        AlertDialog
                alertDialog = new AlertDialog.Builder(context, R.style.customDialog).create();
        alertDialog.setMessage((message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ok", (dialog, which) -> {
            dialog.dismiss();
        });

        WindowManager.LayoutParams
                lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        alertDialog.getWindow().setAttributes(lp);

        LinearLayout.LayoutParams
                layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT); //create a new one
        layoutParams.gravity = Gravity.CENTER; //this is layout_gravity
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setLayoutParams(layoutParams);
    }

    public static void setSavePreferences(
            SharedPreferences
                    sharedPreferences,
            String
                    username,
            String
                    password,
            Integer
                    loginFlag,
            String
                    classDetails,
            String
                    profileDetails,
            String
                    subjectDetails) {

        if (sharedPreferences != null) {

            SharedPreferences.Editor
                    editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putInt("flag", loginFlag);
            editor.putString("classDetails", classDetails);
            editor.putString("profileDetails", profileDetails);
            editor.putString("subjectDetails", subjectDetails);
            editor.apply();
        }
    }
}
