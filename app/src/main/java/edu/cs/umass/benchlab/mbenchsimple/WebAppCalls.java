package edu.cs.umass.benchlab.mbenchsimple;

import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * Created by snehas on 2/23/15.
 */
public class WebAppCalls {

    static JSONObject json;

    public static boolean isNotNull(String txt) {
        return txt != null && txt.trim().length() > 0 ? true : false;
    }

    private static void writeToFile(String filename, String uuid) {

        File file = new File(filename);
        Log.d("benchlab", uuid);
        try {
            OutputStream op = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(op);

            outputStreamWriter.write(uuid);

            outputStreamWriter.close();
        } catch (IOException ex) {

        }
    }


    public static void invokeWebapp_GET(final Context context, RequestParams params, String appPath) {
        AsyncHttpClient client = new AsyncHttpClient();
        json = new JSONObject();
        String ip = GlobalConstants.IP_WEBAPP;
        Log.d(GlobalConstants.LOG_TAG, "http://" + ip + appPath);
        client.setBasicAuth("hehe","haha");

        client.get("http://" + ip + appPath, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        Log.d("benchlab", this.getRequestURI().toString());
                        Header[] headers = this.getRequestHeaders();
                        for (Header hr : headers) {
                            Log.d("benchlab", hr.getName() + "," + hr.getValue());
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] h, byte[] b) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        Log.d("benchlab", "Success");

                        try {
                            json = new JSONObject(new String(b));
                            writeToFile(GlobalConstants.getInstance().getBenchlabDirName() + "meta.txt", json.getString("uuid"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] h, byte[] b, Throwable error) {
                        Toast.makeText(context, "Failed due to: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("benchlab", error.getMessage());
                        for (Header hr : h) {
                            Log.d("benchlab", hr.getName() + "," + hr.getValue());
                        }
                        Log.d("benchlab", "" + statusCode);
                    }
                });

        }



    public static void invokeWebapp_GET_FILE(final Context context, RequestParams params, String appPath, File trace_file) {
        AsyncHttpClient client = new AsyncHttpClient();
        String ip = GlobalConstants.IP_WEBAPP;
        Log.d(GlobalConstants.LOG_TAG, "http://" + ip + appPath);
        client.get("http://" + ip + appPath, params,
                new FileAsyncHttpResponseHandler(trace_file) {

                    @Override
                    public void onStart() {
                        Log.d("benchlab", this.getRequestURI().toString());
                        Header[] headers = this.getRequestHeaders();
                        for (Header hr : headers) {
                            Log.d("benchlab", hr.getName() + "," + hr.getValue());
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] h, File f) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        Log.d("benchlab", "Success");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] h, Throwable error, File f) {
                        Toast.makeText(context, "Failed due to: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("benchlab", error.getMessage());
                        for (Header hr : h) {
                            Log.d("benchlab", hr.getName() + "," + hr.getValue());
                        }
                        Log.d("benchlab", "" + statusCode);
                    }
                });
    }

    public static void invokeWebapp_POST(final Context context, RequestParams params, String appPath) {
        AsyncHttpClient client = new AsyncHttpClient();

        String ip = GlobalConstants.IP_WEBAPP;
        Log.d(GlobalConstants.LOG_TAG, "http://" + ip + appPath);
        client.post("http://" + ip + appPath, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        Log.d("benchlab", this.getRequestURI().toString());
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] h, byte[] b) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        Log.d("benchlab", "Success");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] h, byte[] b, Throwable error) {
                        Toast.makeText(context, "Failed due to: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        for (Header hr : h) {
                            Log.d("benchlab", hr.getName() + "," + hr.getValue());
                        }
                        Log.d("benchlab", error.getMessage());
                        Log.d("benchlab", "" + statusCode);
                    }
                });

    }

}
