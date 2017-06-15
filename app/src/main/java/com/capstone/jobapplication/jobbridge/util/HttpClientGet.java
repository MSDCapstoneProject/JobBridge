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

public class HttpClientGet extends AsyncTask {

    private static String urlPrefix = "http://192.168.0.10:3000";
    private String path = "";

    public HttpClientGet(String path) {
        this.path = path;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urlPrefix+path);
            urlConnection = (HttpURLConnection) url.openConnection();
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
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
