package edu.cs.umass.benchlab.mbenchsimple;
import android.net.TrafficStats;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import com.example.mbenchsimple.R;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.MotionEvent;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebviewActivity extends Activity {

    private static Handler handler;

    private class MyRunnable implements Runnable {

        public void run() {

            ExperimentList expt = ExperimentList.getInstance();
            String url;

            while (true) {
                url = expt.getURL();
                Log.d("benchlab","getting url in run " + url);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Message msg = Message.obtain();
                msg.obj = url;
                msg.setTarget(handler);
                msg.sendToTarget();

                if (url == "null") {
                    break;
                }

                //url = expt.getURL();
            }
        }
    }

    private WebView browser;
    public static String tag = "benchlab";

    private void loadPage() {
        ExperimentList expt = ExperimentList.getInstance();
        String url = expt.getURL();

        if (url != "null") {
            Log.d("benchlab","from load page");
            browser.loadUrl(url);
        } else {
            Intent intent = new Intent(WebviewActivity.this, ResultActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        browser = (WebView) findViewById(R.id.browser);
        browser.getSettings().setJavaScriptEnabled(false);
        browser.setEnabled(false);
        browser.setClickable(false);

        browser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        loadPage();
        registerClient();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String url = (String) msg.obj;
                if (url != "null") {
                    Log.d("benchlab","from handler page " + url);
                    browser.setEnabled(false);
                    browser.setClickable(false);
                    browser.loadUrl(url);
                } else {
                    Intent intent = new Intent(WebviewActivity.this, ResultActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        MyRunnable runnable = new MyRunnable();
        Thread mythread = new Thread(runnable);
        mythread.start();

    }

    private void registerClient() {
        browser.setWebViewClient(new WebViewClient() {

            ResultEntity entity;


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                ExperimentEntity expt = ExperimentList.getInstance().getCurrentExpt();
                entity = new ResultEntity();
                entity.setStart(System.currentTimeMillis());
                entity.setUrl(expt.getURL());
                entity.setTag(expt.getTag());

                /*Fix this!*/
                Log.d("benchlab", expt.getTag() + ":" + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ExperimentList.getInstance();
                /*Update end time*/
                entity.setEnd(System.currentTimeMillis());
                /*Gives bytes downloaded from startup. DO NOT use this!*/
                //entity.setBytes(TrafficStats.getUidRxBytes(android.os.Process.myUid()));

                ExperimentResults.getInstance().updateResults(entity);

                String urlfilename = ExperimentList.getInstance().getCurrentExpt().getTag();

				/*Take a webarchive file*/
                String dirName = GlobalConstants.getInstance().getDirName() + "mhtfiles/";
                File dir = new File(dirName);
                dir.mkdirs();

                String webarchive = dirName + urlfilename + ".mht";
                view.saveWebArchive(webarchive);

				/*Take screen snapshot*/
                dirName = GlobalConstants.getInstance().getDirName() + "snapshots/";
                dir = new File(dirName);
                dir.mkdirs();

                Picture pic = view.capturePicture();
                // Bitmap of the entire document
                int witdh = pic.getWidth();
                int height = pic.getHeight();
                Bitmap raw = Bitmap
                        .createBitmap(witdh, height, Bitmap.Config.RGB_565);

                // Drawing on a canvas
                Canvas cv = new Canvas(raw);
                pic.draw(cv);
                FileOutputStream fos = null;
                try {
                    String path = dirName + urlfilename + ".png";
                    fos = new FileOutputStream(path);
                    if (fos != null) {
                        raw.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                        fos.close();
                    }
                } catch (Exception e) {
                    System.out.println("-----error--" + e);
                }

                Toast.makeText(getApplicationContext(), "Loading experiment: " +
                                ExperimentList.getInstance().getCurrentExpt().getTag(),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }


}
