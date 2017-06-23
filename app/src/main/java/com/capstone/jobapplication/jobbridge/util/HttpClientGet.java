package com.capstone.jobapplication.jobbridge.util;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Aicun on 5/24/2017.
 */

public class HttpClientGet extends AsyncTask<String,Void,String> {

    private static String urlPrefix = "http://192.168.0.10:3000";
    //private static String urlPrefix = "http://142.156.86.79:3000";
    private String path = "";

    public HttpClientGet(String path) {
        this.path = path;
    }

    @Override
    protected String doInBackground(String[] params) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urlPrefix+path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15);
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            byte[] contents = new byte[1024];

            int bytesRead = 0;
            String strFileContents = null;
            while((bytesRead = in.read(contents)) != -1) {
                strFileContents += new String(contents, 0, bytesRead);
            }
            return strFileContents;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
    }

    public String getJsonData() {
        String jsonData = null;
        try {
            AsyncTask task = execute();
            jsonData = (String) task.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonData;
    }
}
