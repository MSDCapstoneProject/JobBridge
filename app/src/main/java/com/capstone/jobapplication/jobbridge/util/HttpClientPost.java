package com.capstone.jobapplication.jobbridge.util;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Aicun on 5/24/2017.
 */

public class HttpClientPost extends AsyncTask<String, Void, String> {

    private static String urlPrefix = "https://jobbridge.herokuapp.com";
    //private static String urlPrefix = "http://192.168.0.10:3000";
    //private static String urlPrefix = "http://142.156.86.79:3000";
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
            urlConnection.setConnectTimeout(15);
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

    public String doPost(Map map) throws ExecutionException, InterruptedException {
        Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
        String jsonForJobApply = JsonConverter.convertFromMap(map,mapType);
        AsyncTask task = execute(jsonForJobApply);
        String result = (String) task.get();
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
