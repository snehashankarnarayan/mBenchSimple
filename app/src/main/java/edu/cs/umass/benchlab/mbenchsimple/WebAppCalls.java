package edu.cs.umass.benchlab.mbenchsimple;

import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.io.File;
import org.apache.http.Header;

/**
 * Created by snehas on 2/23/15.
 */
public class WebAppCalls {

    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    public static void invokeWebapp_GET(final Context context, RequestParams params, String appPath){
        AsyncHttpClient client = new AsyncHttpClient();

        String ip = "10.3.40.219" + ":9998";
        Log.d(GlobalConstants.LOG_TAG, "http://" + ip + appPath);
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

    public static void invokeWebapp_GET_FILE(final Context context, RequestParams params, String appPath, File trace_file){
        AsyncHttpClient client = new AsyncHttpClient();
        String ip = "10.3.40.219" + ":9998";
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

    public static void invokeWebapp_POST(final Context context, RequestParams params, String appPath){
        AsyncHttpClient client = new AsyncHttpClient();

        String ip = "10.3.40.219" + ":9998";
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
