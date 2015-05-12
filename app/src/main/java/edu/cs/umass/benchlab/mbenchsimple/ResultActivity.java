package edu.cs.umass.benchlab.mbenchsimple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.security.spec.X509EncodedKeySpec;

import com.example.mbenchsimple.R;
import com.loopj.android.http.RequestParams;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.security.KeyPair;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

import java.security.KeyFactory;
import java.security.PublicKey;

public class ResultActivity extends Activity {

    private Button exit;

    private void uploadFile() {
        Toast.makeText(getApplicationContext(), "Uploading results", Toast.LENGTH_SHORT).show();
        String zipfilename = GlobalConstants.getInstance().getBenchlabDirName() +
                GlobalConstants.getExperimentName() + ".zip";
        Compress dao = new Compress();
        dao.zipDirectory(GlobalConstants.getInstance().getDirName(), zipfilename);

        uploadZipToWebApp(zipfilename);
    }

    private File encodeFile(File zipfile) {
        File outfile = new File(GlobalConstants.getDirName() + "encfile");
        File publicKeyFile = new File(GlobalConstants.getInstance().getBenchlabDirName() + "id_rsa.pub");
        byte[] encodedKey = new byte[(int)publicKeyFile.length()];
        try {
            new FileInputStream(publicKeyFile).read(encodedKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create public key
        try {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pk = kf.generatePublic(publicKeySpec);

            Cipher pkCipher = Cipher.getInstance("RSA");
            pkCipher.init(Cipher.ENCRYPT_MODE, pk);
            CipherOutputStream os = new CipherOutputStream(new FileOutputStream(outfile), pkCipher);
            byte[] data = new byte[(int)zipfile.length()];
            os.write(new FileInputStream(zipfile).read(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outfile;
    }
    private void uploadZipToWebApp(String zipfilename) {
        //zipfilename = GlobalConstants.getInstance().getDirName() +  "results.csv";
        File zipfile = new File(zipfilename);
        //File encodedFile = encodeFile(zipfile);

        RequestParams params = new RequestParams();

        try {
            params.put("result_zip", zipfile, "multipart/form-data");
            params.put("deviceid", GlobalConstants.getConstantDeviceID());
            params.put("os", ExperimentResults.getInstance().getPhoneData().getVersion());
            params.put("location", ExperimentResults.getInstance().getPhoneData().getLocation());
            params.put("ip", ExperimentResults.getInstance().getPhoneData().getIpAddress());
            params.put("exptNumber", GlobalConstants.getExperimentName());
            params.put("magicNumber", ExperimentResults.getInstance().getMagicNumber());

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        WebAppCalls.invokeWebapp_POST(getApplicationContext(), params,
                GlobalConstants.PATH_UPLOADRESULTS);

        //File movedZipFile = new File(GlobalConstants.getDirName() + GlobalConstants.getExperimentName() + ".zip");
        //zipfile.renameTo(movedZipFile);

    }

    private void makeFile(LinkedList<ResultEntity> results) {

        String filename = GlobalConstants.getInstance().getDirName() + "results.csv";
        File file = new File(filename);

        try {
            OutputStream op = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(op);


            for (ResultEntity res : results) {
                outputStreamWriter.write(res.getTag() + "," + res.getLatency() + "\n");

            }
            outputStreamWriter.close();
        } catch (IOException ex) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ExperimentResults inst = ExperimentResults.getInstance();
        LinkedList<ResultEntity> results = inst.getResults();


		/*Table stuff*/
        TableLayout table = new TableLayout(this);
        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);
        TableRow tableTitle = new TableRow(this);
        tableTitle.setGravity(Gravity.CENTER_HORIZONTAL);

		/*Add a title*/
        TextView title = new TextView(this);
        title.setText("Latency results");
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        title.setGravity(Gravity.CENTER);
        title.setBackgroundColor(color.darker_gray);
        tableTitle.addView(title, new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, (float) 0.50));
        table.addView(tableTitle);


		/*Now the site and latency labels*/
        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView siteName = new TextView(this);
        siteName.setText("Web site");
        siteName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        siteName.setGravity(Gravity.CENTER_HORIZONTAL);
        rowTitle.addView(siteName);

        TextView latency = new TextView(this);
        latency.setText("Latency in ms");
        latency.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        latency.setGravity(Gravity.CENTER_HORIZONTAL);
        rowTitle.addView(latency);

        table.addView(rowTitle);

        double mn = 0;

        for (int i=0; i < results.size()-1; i++) {
            ResultEntity res = results.get(i);
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER);

            TextView rowSite = new TextView(this);
            rowSite.setText(res.getTag());
            rowSite.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            rowSite.setGravity(Gravity.CENTER_HORIZONTAL);
            row.addView(rowSite);

            TextView rowLatency = new TextView(this);
            rowLatency.setText(res.getLatency() + "");
            rowLatency.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            rowLatency.setGravity(Gravity.CENTER_HORIZONTAL);
            row.addView(rowLatency);

            table.addView(row);
            mn += res.getLatency();
        }

        mn = mn/results.size();
        ExperimentResults.getInstance().setMagicNumber(mn);

        TableRow mnRow = new TableRow(this);
        mnRow.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView mnView = new TextView(this);
        mnView.setText("Your score: " + mn);
        mnView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mnView.setGravity(Gravity.CENTER_HORIZONTAL);
        mnRow.addView(mnView);
        table.addView(mnRow);

        TableRow exitRow = new TableRow(this);
        exitRow.setGravity(Gravity.CENTER_HORIZONTAL);
        exit = new Button(this);
        exit.setText("Exit");
        exitRow.addView(exit);
        table.addView(exitRow);

        /* Add upload results button */
      /*  TableRow row = new TableRow(this);
        uploadResults = new Button(getApplicationContext());
        row.setGravity(Gravity.CENTER);
        uploadResults.setText("Upload Results");
        uploadResults.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        uploadResults.setGravity(Gravity.CENTER_HORIZONTAL);
        row.addView(uploadResults);
        table.addView(row);*/

        setContentView(table);

        makeFile(results);
        uploadFile();

        /*Deal with onclick of exit button*/
        exit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                //android.os.Process.killProcess(android.os.Process.myPid());
                //super.onDestroy();

            }
        });
    }
}
