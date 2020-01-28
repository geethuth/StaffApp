package com.sieva.staffapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sieva.staffapp.R;
import com.sieva.staffapp.httpRequest.CustomHttpClient;
import com.sieva.staffapp.pojoclass.SubjectPOJO;
import com.sieva.staffapp.util.PreferenceUtil;
import com.sieva.staffapp.util.ServerUtils;
import com.sieva.staffapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MarkentryFragment extends Fragment {

    Spinner std_spinner,subject_spinner,exam_spinner;
    String std,subject,exam;
    ProgressDialog pdia;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_markentry, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        final TextView toolbarmsg = root.findViewById(R.id.toolbar_msg);
        std_spinner=root.findViewById(R.id.std_spinner);
        subject_spinner=root.findViewById(R.id.subject_spinner);
        exam_spinner=root.findViewById(R.id.exam_spinner);
        progressBar=root.findViewById(R.id.progressBar);

        AdapterView.OnItemSelectedListener listner =new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(std_spinner.getSelectedItem()!=null)
                    std=std_spinner.getSelectedItem().toString();
                if(subject_spinner.getSelectedItem()!=null)
                    subject=subject_spinner.getSelectedItem().toString();
                if(subject_spinner.getSelectedItem()!=null)
                    exam=subject_spinner.getSelectedItem().toString();

                if(std!=null&&subject!=null&&exam!=null){
                    if(isNetworkAvailable()){
                        String urls = ServerUtils.ServerUrl;
                        String examMarksURL = ServerUtils.FetchExamMarksApi;

                        ArrayList params = new ArrayList();
                        Pair pair = new Pair("operation", "marksreport");
                        params.add(pair);
                        pair = new Pair("standard", std);
                        params.add(pair);
                        pair = new Pair("subject", subject);
                        params.add(pair);
                        pair = new Pair("exam", exam);
                        params.add(pair);

                        String postString = "payload=" + Utils.createPostString(params);

                        Log.d("exam_marks##","--"+examMarksURL+ "---"+postString);
                        WebView wv1 = (WebView) root.findViewById(R.id.webview);
                        wv1.setWebViewClient(new MyBrowser());
                        wv1.getSettings().setLoadWithOverviewMode(true);
                        wv1.getSettings().setUseWideViewPort(true);
                        wv1.getSettings().setLoadsImagesAutomatically(true);
                        wv1.getSettings().setJavaScriptEnabled(true);
                        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                        wv1.getSettings().setDomStorageEnabled(true);
                        wv1.postUrl(examMarksURL, postString.getBytes());
                    } else{

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        std_spinner.setOnItemSelectedListener(listner);
        subject_spinner.setOnItemSelectedListener(listner);
        exam_spinner.setOnItemSelectedListener(listner);

        toolbarmsg.setText("Mark Entry");
        ArrayList<String> division_details=new ArrayList<>();
        ArrayList<String> subjects=new ArrayList<>();
        ArrayList<String> exams=new ArrayList<>();
        if(PreferenceUtil.subjectDetailsArray!=null){
            for(int i=0;i<PreferenceUtil.subjectDetailsArray.size();i++){
                SubjectPOJO subjectPOJO=PreferenceUtil.subjectDetailsArray.get(i);
                division_details.add(subjectPOJO.getSubjectClassName()+" - "+subjectPOJO.getSubjectClassDivision());
                subjects.add(subjectPOJO.getSubjectName());
            }
            String[] division = division_details.toArray(new String[division_details.size()]);
            String[] subject = subjects.toArray(new String[subjects.size()]);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, division);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            std_spinner.setAdapter(adapter);

            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subject);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subject_spinner.setAdapter(adapter);
        }
        std =std_spinner.getSelectedItem().toString();
        subject=subject_spinner.getSelectedItem().toString();

        Log.d("mark#entry","##"+std+"  ### "+subject);

        getExamDetails(std,subject);
        return root;
    }

    private void getExamDetails(String std, String subject) {
        boolean is_connected=false;
        if(getActivity()!=null) {
            if(isNetworkAvailable()){
                String urls = ServerUtils.ServerUrl;
                String FetchExamServerUrl = ServerUtils.FetchExamsApi;
                Log.d("MarksEntry url", FetchExamServerUrl);
                try {
                    ArrayList<Pair> params = new ArrayList<>();
                    Pair<String, String> pair = new Pair<>("operation", "getexams");
                    params.add(pair);
                    pair=new Pair<>("standard",std);
                    params.add(pair);
                    pair=new Pair<>("subject",subject);
                    params.add(pair);

                    String postString = Utils.createPostString(params);
                    Log.i("param", postString);

                    FetchExamsTask fetchExamsTask=new FetchExamsTask();
                    fetchExamsTask.execute(FetchExamServerUrl,postString);

                   //Log.d("subject_details","--"+ PreferenceUtil.subjectDetailsArray);


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                       return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            }else{

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_statut", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_stats", "" + e.getMessage());
                }
            }
    }
        return false;
}

    private class FetchExamsTask extends AsyncTask<String,Void,String> {

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
            String res = null;
            try {
                res = CustomHttpClient.executeHttpPost(url[0], url[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            res="{\"status\":\"true\",\"message\":\"success\",\n" +
                    "\"exams\":[{\"name\":\"First Exam\"},\n" +
                    "{\"name\":\"Second Exam\"}]\n" +
                    "}";
            return res;
        }

        @Override
        protected void onPostExecute(String response) {
            if (pdia != null && pdia.isShowing()) {
                pdia.dismiss();
            }
            if (response != null && !response.equals("")) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.get("status").equals("true")){
                        JSONArray jsonArray=jsonObject.getJSONArray("exams");
                        String []exams=new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject examsObj=jsonArray.getJSONObject(i);
                            exams[i]=examsObj.get("name").toString();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, exams);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        exam_spinner.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }
    }

    private void dismissProgressDialog() {
        if (pdia != null && pdia.isShowing()) {
            pdia.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        //isDestroyed = true;
        dismissProgressDialog();
        super.onDestroy();
    }

}