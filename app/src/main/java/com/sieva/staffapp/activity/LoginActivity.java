package com.sieva.staffapp.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout rellay1, rootview;
    TextView appName;
    EditText userText, passText;
    Animation animation;
    Button login;
    private ProgressDialog pdia;
    String response;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            appName.clearAnimation();
            rellay1.setVisibility(VISIBLE);
            appName.setVisibility(GONE);

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        rellay1 = findViewById(R.id.relley1);
        rootview = findViewById(R.id.rootView);
        appName = findViewById(R.id.app_name);
        userText = findViewById(R.id.username);
        passText = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        appName.setAnimation(animation);
        handler.postDelayed(runnable, 3000);
    }

    private void loginAPI() {
        String urls = ServerUtils.ServerUrl;
        String loginServerUrl = ServerUtils.LoginApi;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
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
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Server connectivity issue..", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class LoginTask extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(LoginActivity.this);
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
                            if ((response.contains("profile")) && (response.contains("subjects")) && (response.contains("class"))) {
                                Utils.setSavePreferences(
                                        SplashScreen.sharedPreferences,
                                        PreferenceUtil.username,
                                        PreferenceUtil.password,
                                        PreferenceUtil.flag,
                                        respjson.getJSONObject("profile").toString(),
                                        respjson.getJSONArray("subjects").toString(),
                                        respjson.getJSONArray("class").toString());
                            }
                            Intent obj_intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(obj_intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AlertDialog WrongPasswordAlert = new AlertDialog.Builder(LoginActivity.this).create();
                        WrongPasswordAlert.setTitle("Login Failed");
                        WrongPasswordAlert.setMessage("Please type correct credentials to login");
                        WrongPasswordAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                (dialog, which) -> dialog.dismiss());
                        WrongPasswordAlert.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }//close onPostExecute

        @Override
        public void onCancel(DialogInterface dialogInterface) {
        }
    }

    public void loginCheck(View view) {
        PreferenceUtil.username = userText.getText().toString();
        PreferenceUtil.password = passText.getText().toString();
        System.out.println("username:" + PreferenceUtil.username);
        System.out.println("pass:" + PreferenceUtil.password);
        if (!userText.getText().toString().equals("") && !passText.getText().toString().equals("")) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (PreferenceUtil.username != null && PreferenceUtil.password != null) {
                    loginAPI();
                }else{
                    Utils.showAlertMessage(this,"User details is not available now");
                }
            } else {
                Toast.makeText(this, "Please check your internet connectivity..", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplication(), "Please enter your credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
