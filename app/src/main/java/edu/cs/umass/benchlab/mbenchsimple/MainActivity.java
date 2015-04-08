package edu.cs.umass.benchlab.mbenchsimple;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;
import android.util.Log;

import com.example.mbenchsimple.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.widget.Toast;


public class MainActivity extends Activity {

    private Button start;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("benchlab", "START");


        start = (Button) findViewById(R.id.start);
        register = (Button) findViewById(R.id.register);

        GlobalConstants inst = GlobalConstants.getInstance();
        String experimentName = "expt_" + System.currentTimeMillis();
        String dirname = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/benchlab/" + experimentName + "/";

        File dir = new File(dirname);
        dir.mkdirs();
        inst.setDirName(dirname);
        inst.setBenchlabDirName(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/benchlab/");
        inst.setExperimentName(experimentName);

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

}
