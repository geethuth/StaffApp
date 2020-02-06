package com.sieva.staffapp.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.sieva.staffapp.R;
import com.sieva.staffapp.activity.LoginActivity;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class ChangePasswordFragment extends Fragment {

    public static SharedPreferences pref;
    public static Context context;
    public SharedPreferences.Editor editor;
    private ProgressDialog pdia;
    private String response;
    public static String old_password_str, new_password_str, confirm_new_password_str;
    public static ConnectivityManager connectivityManager;

    public ChangePasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View
                attendanceView = inflater.inflate(
                R.layout.fragmet_changepassword,
                container,
                false);

        final TextView toolbarmsg = attendanceView.findViewById(R.id.toolbar_msg);
        toolbarmsg.setText("Change Password");
        ImageView
                toolbar_back_button = attendanceView.findViewById(R.id.toolbar_back_button);
        toolbar_back_button.setVisibility(View.VISIBLE);
        if (getActivity() != null) {
            toolbar_back_button.setOnClickListener((View v) -> getActivity().onBackPressed());
        }

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        context = getActivity();
        pref = context.getSharedPreferences("DataStore", MODE_PRIVATE);
        editor = pref.edit();

        final EditText old_password_txt = attendanceView.findViewById(R.id.oldPassword);
        final EditText new_password_txt = attendanceView.findViewById(R.id.newPassword);
        final EditText confirm_new_password_txt = attendanceView.findViewById(R.id.confirmPassword);
        Button submit = attendanceView.findViewById(R.id.submit);
        submit.setOnClickListener(view -> {
            if (!old_password_txt.getText().toString().equals("") && !new_password_txt.getText().toString().equals("") && !confirm_new_password_txt.getText().toString().equals("")) {
                old_password_str = old_password_txt.getText().toString().trim();
                new_password_str = new_password_txt.getText().toString().trim();
                confirm_new_password_str = confirm_new_password_txt.getText().toString().trim();
                System.out.println("old:" + old_password_str);
                System.out.println("new_password_str:" + new_password_str);
                System.out.println("confirm_new_password_str:" + confirm_new_password_str);
                if (old_password_str.equals(PreferenceUtil.password)) {
                    if (!old_password_str.equals(new_password_str)) {
                        if (new_password_str.equals(confirm_new_password_str)) {
                            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                                resetPasswordAPI();
                            } else {
                                Toast.makeText(context, getString(R.string.internet_connectivity_check), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                            builder.setMessage(getString(R.string.confirm_password_correctly))
                                    .setCancelable(false)
                                    .setNeutralButton("OK", (dialog, id) -> dialog.cancel());
                            android.app.AlertDialog alert = builder.create();
                            alert.show();
                        }
                    } else
                        Toast.makeText(context, (getString(R.string.enter_new_password)), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, (getString(R.string.enter_correct_old_password)), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, (getString(R.string.enter_valid_data_for_fields)), Toast.LENGTH_SHORT).show();
            }
        });

        /*********************************************************************************************/

        return attendanceView;
    }

    private void resetPasswordAPI() {

        String urls = ServerUtils.ServerUrl;
        String AttendanceServerUrl = ServerUtils.submitAttendanceAPI;
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            String AttendanceListUrl = urls + AttendanceServerUrl;// Serverfilenames.loginUrl;
            Log.d("Attendance url", AttendanceListUrl);
            try {

                //$payload = "operation::submitattendance###year::2020###month::01###date::13/12/2019###present::{12,13,15,32,23}###absent::{87,10}";
                ArrayList<Pair> params = new ArrayList<>();
                Pair<String, String> pair = new Pair<>("operation", "changepassword");
                params.add(pair);
                pair = new Pair<>("authcode", PreferenceUtil.authCode);
                params.add(pair);
                pair = new Pair<>("oldpassword", old_password_str);
                params.add(pair);
                pair = new Pair<>("newpassword", new_password_str);
                params.add(pair);

                String postString = Utils.createPostString(params);
                Log.i("param", postString);
                ForgotTask forgotTask = new ForgotTask();
                forgotTask.execute(AttendanceListUrl, postString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Server connectivity issue..", Toast.LENGTH_SHORT).show();

        }
    }


    @SuppressLint("StaticFieldLeak")
    public class ForgotTask extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getContext());
            pdia.setMessage("please wait..");
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String res = null;
            try {
                response = CustomHttpClient.executeHttpPost(url[0], url[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pdia != null && pdia.isShowing()) {
                pdia.dismiss();
            }
            System.out.println("Enable_student response: " + response);
            if (response != null && !response.equals("")) {
                if (response.contains("success")) {
                    AlertDialog SuccessAlert = new AlertDialog.Builder(Objects.requireNonNull(getContext())).create();
                    SuccessAlert.setMessage("Password added successfully");
                    SuccessAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialogInterface, i) -> {
                                if (getActivity() != null) {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                    );
                    SuccessAlert.show();
                }
            }
        }//close onPostExecute

        @Override
        public void onCancel(DialogInterface dialogInterface) {
        }
    }


}
