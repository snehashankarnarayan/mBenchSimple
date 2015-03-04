package edu.cs.umass.benchlab.mbenchsimple;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Build;
import com.example.mbenchsimple.R;
import com.loopj.android.http.RequestParams;
import java.io.File;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;
public class RegisterActivity extends ActionBarActivity {

    private Button start;
    private Button trace;

    public String getDeviceUuid() {
        //return Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        return UUID.randomUUID().toString();
    }

    private void registerUuid() {

        RequestParams params = new RequestParams();
        params.put("deviceid", getDeviceUuid());
        params.put("devicename", Build.MODEL);

        WebAppCalls.invokeWebapp_GET(getApplicationContext(), params, GlobalConstants.PATH_REGISTER);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        start = (Button) findViewById(R.id.start);
        trace = (Button) findViewById(R.id.trace);

        registerUuid();

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
