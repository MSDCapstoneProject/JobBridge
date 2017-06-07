package com.capstone.jobapplication.jobbridge.util;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aicun on 5/24/2017.
 */

public class HttpClientPost extends AsyncTask {

    private static String urlPrefix = "http://192.168.0.14:3000";
    private String path = "";

    public HttpClientPost(String path) {
        this.path = path;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urlPrefix + path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            String json = (String) params[0];
            out.write(json);
            out.flush();
            out.close();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
