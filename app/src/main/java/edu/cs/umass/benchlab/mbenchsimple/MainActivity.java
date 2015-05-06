package edu.cs.umass.benchlab.mbenchsimple;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import android.content.Context;
import android.os.Build;
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
import java.io.BufferedReader;
import java.io.FileReader;


public class MainActivity extends Activity {

    private Button start;
    private Button register;
    private Button results;

    private void writeToFile(String filename, byte[] b) {

        File file = new File(filename);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
            bos.write(b);
            bos.flush();
            bos.close();


        } catch (IOException ex) {

        }

    }

    public String generateDeviceUuid() {

        GlobalConstants.getInstance().setConstantDeviceID(UUID.randomUUID().toString());
        return GlobalConstants.getInstance().getConstantDeviceID();
    }


    private void registerUuid() {

        RequestParams params = new RequestParams();
        params.put("deviceid", generateDeviceUuid());
        File file = new File(GlobalConstants.getBenchlabDirName() + "meta.txt");

            try {
                OutputStream op = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(op);

                outputStreamWriter.write(GlobalConstants.getConstantDeviceID());

                outputStreamWriter.close();
            } catch (IOException ex) {

            }

        params.put("devicename", Build.MODEL);


        WebAppCalls.invokeWebapp_GET(getApplicationContext(), params, GlobalConstants.PATH_REGISTER);
//        Log.d("benchlab", "first:" + uuid);
//        writeToFile(GlobalConstants.getInstance().getBenchlabDirName() + "meta.txt", uuid);
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair keyPair = kpg.genKeyPair();
            byte[] pri = keyPair.getPrivate().getEncoded();
            byte[] pub = keyPair.getPublic().getEncoded();
            writeToFile(GlobalConstants.getInstance().getBenchlabDirName() + "id_rsa.pk", pri);
            writeToFile(GlobalConstants.getInstance().getBenchlabDirName() + "id_rsa.pub", pub);

            params = new RequestParams();
            try {
                params.put("deviceid", GlobalConstants.getConstantDeviceID());
                params.put("key", new File(GlobalConstants.getInstance().getBenchlabDirName() + "id_rsa.pk"), "multipart/form-data");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

            WebAppCalls.invokeWebapp_POST(getApplicationContext(), params,
                    GlobalConstants.PATH_KEY);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }


    }

    private boolean checkFile() {
        String filename = GlobalConstants.getBenchlabDirName() + "meta.txt";
        File file = new File(filename);
        if(file.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void readAndSetUUID() {
        BufferedReader br;
        try {
        FileReader fr = new FileReader(GlobalConstants.getBenchlabDirName() + "meta.txt");
        br = new BufferedReader(fr);

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            String everything = sb.toString();
            GlobalConstants.setConstantDeviceID(everything);

        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("benchlab", "START");


        start = (Button) findViewById(R.id.start);
        register = (Button) findViewById(R.id.register);
        results = (Button) findViewById(R.id.results);

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

        boolean regCheck = checkFile();

        if(!regCheck) {
            Toast.makeText(getApplicationContext(), "Registering device...", Toast.LENGTH_LONG).show();
            registerUuid();
            RequestParams params = new RequestParams();
            params.put("deviceID",GlobalConstants.getConstantDeviceID());
            String trace_file_name = GlobalConstants.getBenchlabDirName() + "trace.txt";
            File trace = new File(trace_file_name);
            try {
                trace.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            WebAppCalls.invokeWebapp_GET_FILE(getApplicationContext(), params, GlobalConstants.PATH_GET_TRACE, trace);
            start.setVisibility(View.VISIBLE);

        }
        else
        {
            start.setVisibility(View.VISIBLE);
        }

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                readAndSetUUID();
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

        results.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultViews.class);
                startActivity(intent);

            }
        });
    }

}
