package edu.cs.umass.benchlab.mbenchsimple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

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

public class ResultActivity extends Activity {

    private Button exit;

    public String getDeviceUuid() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void uploadFile()
    {
        Toast.makeText(getApplicationContext(), "Uploading results", Toast.LENGTH_SHORT).show();
        String zipfilename = GlobalConstants.getInstance().getBenchlabDirName() +
                GlobalConstants.getExperimentName() + ".zip";
        Compress dao = new Compress();
        dao.zipDirectory(GlobalConstants.getInstance().getDirName(), zipfilename);

        uploadZipToWebApp(zipfilename);
    }

    private void uploadZipToWebApp(String zipfilename) {
        //zipfilename = GlobalConstants.getInstance().getDirName() +  "results.csv";
        File zipfile = new File(zipfilename);
        RequestParams params = new RequestParams();
        try {
            params.put("result_zip", zipfile, "multipart/form-data");
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }

        WebAppCalls.invokeWebapp_POST(getApplicationContext(), params,
                GlobalConstants.PATH_UPLOADRESULTS);

        //File movedZipFile = new File(GlobalConstants.getDirName() + GlobalConstants.getExperimentName() + ".zip");
        //zipfile.renameTo(movedZipFile);

    }

	private void makeFile(LinkedList<ResultEntity> results)
	{
		 
		String filename = GlobalConstants.getInstance().getDirName() +  "results.csv";
		File file = new File(filename);

		try
		{
			OutputStream op = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(op);


			for(ResultEntity res: results)
			{
				outputStreamWriter.write(res.getTag() + "," + res.getLatency() + "\n");

			}
			outputStreamWriter.close();
		}
		catch(IOException ex)
		{

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
		tableTitle.addView(title, new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, (float)0.50));
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

		for(ResultEntity res: results)
		{
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
		}

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
