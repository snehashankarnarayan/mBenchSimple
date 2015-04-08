package edu.cs.umass.benchlab.mbenchsimple;

import android.util.Log;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Build;
import java.io.BufferedOutputStream;

import com.example.mbenchsimple.R;
import com.loopj.android.http.RequestParams;

import java.io.File;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import javax.crypto.Cipher;

public class RegisterActivity extends ActionBarActivity {

    private Button start;
    private Button trace;

    public String generateDeviceUuid() {

        GlobalConstants.getInstance().setConstantDeviceID(UUID.randomUUID().toString());
        return GlobalConstants.getInstance().getConstantDeviceID();
    }

    private void writeToFile(String filename, String uuid) {

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

    private void registerUuid() {

        RequestParams params = new RequestParams();
        params.put("deviceid", generateDeviceUuid());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        start = (Button) findViewById(R.id.start);
        trace = (Button) findViewById(R.id.trace);
        trace.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "Registering device...", Toast.LENGTH_LONG).show();
        registerUuid();
        trace.setVisibility(View.VISIBLE);
        trace.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
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
        });

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, ConfigActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
