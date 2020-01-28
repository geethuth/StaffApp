package com.sieva.staffapp.httpRequest;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

public class CustomHttpClient {
    /**
     * The time it takes for our client to timeout
     */
    public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds

    static String ret;
    private static final String TAG = "CustomHttpClient";
    //public static WebkitCookieManagerProxy coreCookieManager;
    public static OkHttpClient OkHTTPClient;

    // Send a POST request
    public static String executeHttpPost(String url, String postString) throws Exception {
        // Log.d("POST Request", url);
        InputStream in = null;
        Request request = null;

        try {
            RequestBody body = new FormEncodingBuilder()
                    .add("payload", postString)
                    .build();
            request = new Request.Builder().url(url).post(body).build();

            Response response = getClient().newCall(request).execute();
            ret = response.body().string();

            // Get cookieManager content
            //Log.d(url, SplashScreen.coreCookieManager.retCookie(url));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    // Send a GET request
    public static String executeHttpPost(String url) throws Exception {
        // Log.d("GET Request", url);
        InputStream in = null;
        Request request = null;

        try {
            request = new Request.Builder().url(url).build();
            Response response = getClient().newCall(request).execute();
            ret = response.body().string();

            // Get cookieManager content
            //Log.d(url, coreCookieManager.retCookie(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static OkHttpClient getClient() {
        if (OkHTTPClient == null) {
            OkHTTPClient = new OkHttpClient();

            // initialize WebkitCookieManagerProxy, set Cookie Policy, create android.webkit.webkitCookieManager
//            coreCookieManager = new WebkitCookieManagerProxy(null, java.net.CookiePolicy.ACCEPT_ALL);
//            CookieHandler.setDefault(coreCookieManager);
//            OkHTTPClient.setCookieHandler(coreCookieManager);
        }
        return OkHTTPClient;
    }
}
