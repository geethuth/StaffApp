package com.sieva.staffapp.activity;

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
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sieva.staffapp.R;
import com.sieva.staffapp.helper.ClassDetailsHelper;
import com.sieva.staffapp.helper.StaffDetailsHelper;
import com.sieva.staffapp.helper.SubjectDetailsHelper;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class SplashScreen extends AppCompatActivity {

    public static SharedPreferences
            sharedPreferences;
    public SharedPreferences.Editor editor;
    public static final String
            Username = "username", Password = "password", Flag = "flag";
    private ProgressDialog pdia;
    String response;
    private CountDownTimer
            countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.contains(Username) && sharedPreferences.contains(Password) && sharedPreferences.contains(Flag)) {
            String
                    usernameString = sharedPreferences.getString(Username, "");
            String
                    passwordString = sharedPreferences.getString(Password, "");
            Integer
                    flagValue = sharedPreferences.getInt(Flag, 0);
            System.out.println("username:" + usernameString);
            System.out.println("passwordString:" + passwordString);
            System.out.println("flag:" + flagValue);
            if (usernameString != null && passwordString != null && flagValue != 0) {
                PreferenceUtil.username = usernameString;
                PreferenceUtil.password = passwordString;
                if (PreferenceUtil.username != null && PreferenceUtil.password != null) {
                    getData();
                } else {
                    Utils.showAlertMessage(this, "User details is not available now");
                }
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void loginAPI() {
        String urls = ServerUtils.ServerUrl;
        String loginServerUrl = ServerUtils.LoginApi;
        String loginUrl = urls + loginServerUrl;// Serverfilenames.loginUrl;
        Log.d("login url", loginUrl);
        try {
            ArrayList<Pair> params = new ArrayList<>();
            Pair<String, String> pair = new Pair<>("operation", "stafflogin");
            params.add(pair);
            pair = new Pair<>("username", PreferenceUtil.username);
            params.add(pair);
            pair = new Pair<>("password", PreferenceUtil.password); // android.telephony.TelephonyManager.getDeviceId()
            params.add(pair);
            String postString = Utils.createPostString(params);
            Log.i("param", postString);
            LoginTask loginTask = new LoginTask();
            loginTask.execute(loginUrl, postString);
        } catch (Exception e) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            displayRetryAlertDialog();
            e.printStackTrace();
        }
    }

    private void displayRetryAlertDialog() {
        AlertDialog SuccessAlert = new AlertDialog.Builder(SplashScreen.this).create();
        SuccessAlert.setTitle("NETWORK ERROR");
        SuccessAlert.setMessage(getString(R.string.an_error_occured_while_loading_data_please_check_your_interneet_connectivity));
        SuccessAlert.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", (dialogInterface, i) -> {
            getData();
        });
        SuccessAlert.show();
    }


    @SuppressLint("StaticFieldLeak")
    public class LoginTask extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(SplashScreen.this);
            pdia.setMessage("Validating credentials..");
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String res = null;
            try {
                response = CustomHttpClient.executeHttpPost(url[0], url[1]);
                System.out.println("Enable_login response: " + response);

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
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            System.out.println("Enable_login response: " + response);

            if (response != null && !response.equals("")) {
                JSONObject respjson = new JSONObject();
                try {
                    respjson = new JSONObject(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    //Log.d(TAG, response);
                    if (response != null && response.contains("true")) {
                        try {
                            //  JSONObject datajson = respjson.getJSONObject("profile");
                            String authcode = respjson.getString("authcode");
                            System.out.println("authcode   " + authcode);
                            PreferenceUtil.authCode = authcode;
                            PreferenceUtil.flag = 1;

                            if (response.contains("profile")) {
                                JSONObject datajson = respjson.getJSONObject("profile");
                                PreferenceUtil.staffDetailsArray = StaffDetailsHelper.getStaffList(datajson.toString());
                            }
                            if (response.contains("subjects")) {
                                JSONArray
                                        subjectArray = respjson.getJSONArray("subjects");
                                PreferenceUtil.subjectDetailsArray = SubjectDetailsHelper.getSubjectList(subjectArray.toString());
                            }
                            if (response.contains("class")) {
                                JSONArray
                                        classArray = respjson.getJSONArray("class");
                                PreferenceUtil.classDetailsArray = ClassDetailsHelper.getClassList(classArray.toString());
                            }
                            Utils.setSavePreferences(
                                    SplashScreen.sharedPreferences,
                                    PreferenceUtil.username,
                                    PreferenceUtil.password,
                                    PreferenceUtil.flag,
                                    respjson.getJSONObject("profile").toString(),
                                    respjson.getJSONArray("subjects").toString(),
                                    respjson.getJSONArray("class").toString());
                            Intent obj_intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(obj_intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        getData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getData();
                }

            } else {
                getData();
            }
        }//close onPostExecute

        @Override
        public void onCancel(DialogInterface dialogInterface) {
        }
    }

    public void getData() {
        ConnectivityManager
                connectivityManager = (ConnectivityManager)
                Objects.requireNonNull(getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo
                activeNetworkInfo;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                startCountDowntimer();
                loginAPI();
            } else {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                startCountDowntimer();
            }
        } else {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            startCountDowntimer();
        }
    }

    private void startCountDowntimer() {
        Log.d("startCountDowntimer", "called");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Log.d("startCountDowntimer", "called1");
        countDownTimer = new CountDownTimer(20000, 10000) {
            public void onTick(long millisUntilFinished) {
                Log.e("startCountDowntimer", "" + String.format("%02d:%02d", (millisUntilFinished % 3600) / 60, (millisUntilFinished % 60)));

            }

            public void onFinish() {

                Log.d("startCountDowntimer", "finish");
                displayRetryAlertDialog();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }
}
