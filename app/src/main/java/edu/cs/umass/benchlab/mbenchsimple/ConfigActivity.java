package edu.cs.umass.benchlab.mbenchsimple;

import android.content.Intent;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mbenchsimple.R;
import android.net.*;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
public class ConfigActivity extends ActionBarActivity {

    private Button start;
    private EditText ipValue;
    private EditText locationValue;
    private EditText osValue;

    private void loadExperiments() {
        try {
            File trace = new File(GlobalConstants.getBenchlabDirName() + "trace.txt");
            if(trace.exists()) {
                ExperimentList ex = ExperimentList.getInstance(GlobalConstants.getBenchlabDirName() + "trace.txt");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        start = (Button) findViewById(R.id.start);
        ipValue = (EditText) findViewById(R.id.IPValue);
        locationValue = (EditText) findViewById(R.id.LocationValue);
        osValue = (EditText) findViewById(R.id.OSValue);

        Toast.makeText(getApplicationContext(), "Getting details of your device", Toast.LENGTH_SHORT).show();


        /*Setting the computed text*/
        ipValue.setText(getIP());
        locationValue.setText(getLocation());
        osValue.setText(getOS());

        loadExperiments();

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /* Updating the experiment data */
                ExperimentResults inst = ExperimentResults.getInstance();
                PhoneExperimentData pd = new PhoneExperimentData();
                pd.setIpAddress(ipValue.getText().toString());
                pd.setLocation(locationValue.getText().toString());
                pd.setVersion(osValue.getText().toString());
                inst.updatePhoneData(pd);

                Intent intent = new Intent(ConfigActivity.this, WebviewActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_config, menu);
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

    private String getLocation() {

        if(isGPSEnabled()) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);

            if(location != null) {
                return location.getLatitude() + "," + location.getLongitude();
            }

        }

        return "Enter your location. Cannot retrieve GPS co-ordinates";

    }

    private boolean isGPSEnabled() {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!enabled) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getApplicationContext());
            alertDialogBuilder
                    .setMessage("GPS is disabled in your device. Enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }

         enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        return enabled;

    }

    private String getOS() {
        return Build.VERSION.RELEASE;

    }

    private String getIP() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        DhcpInfo d = wifi.getDhcpInfo();
        return intToIp(d.ipAddress);
    }

    private String intToIp(int addr) {
        return  ((addr & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF));
    }

}
