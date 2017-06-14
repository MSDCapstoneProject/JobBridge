package com.capstone.jobapplication.jobbridge.entity.jobbridge.util;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aicun on 5/24/2017.
 */

public class HttpClientPost extends AsyncTask<String, Void, String> {

    private static String urlPrefix = "http://192.168.0.10:3000";
    private String path = "";

    public HttpClientPost(String path) {
        this.path = path;
    }

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urlPrefix + path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            String json = (String) params[0];
            out.write(json);
            out.flush();
            out.close();

            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            byte[] contents = new byte[1024];
            int bytesRead = 0;
            String strFileContents = null;
            while ((bytesRead = in.read(contents)) != -1) {
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
