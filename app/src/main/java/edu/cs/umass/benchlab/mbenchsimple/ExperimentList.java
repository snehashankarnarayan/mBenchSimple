package edu.cs.umass.benchlab.mbenchsimple;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.util.LinkedList;

public class ExperimentList {

    private static ExperimentList instance = null;
    private static int numTests = 0;
    /*private static String[] urls = {

		"http://www.google.com",
		"http://www.facebook.com",
		"http://www.umass.edu",
		"http://www.yahoo.com",
		"http://www.cnn.com",
		"http://www.isro.org"
		/*"www.wikipedia.org",
		"www.amazon.com",
		"www.twitter.com",
		"www.qq.com",
		"www.taobao.com",
		"www.linkedin.com",
		"www.google.co.in",
		"www.live.com",
	};*/

    private static boolean[] done;
    private static int counter = 0;
    private static LinkedList<ExperimentEntity> expt;

    public static ExperimentList getInstance(String trace_file) {
        if (instance == null) {
            instance = new ExperimentList(trace_file);
        }
        return instance;
    }

    public static ExperimentList getInstance() {
        if (instance == null) {
            instance = new ExperimentList();
        }
        return instance;
    }

    ExperimentList() {

    }

    ExperimentList(String trace_file) {
        expt = new LinkedList<ExperimentEntity>();
        try {
            File trace = new File(trace_file);
            FileReader fr = new FileReader(trace);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                String tag = words[0];
                String url = words[1];
                if (!(url.contains("http://") || url.contains("https://"))) {
                    url = "http://" + url;
                }

                ExperimentEntity entity = new ExperimentEntity(tag, url);
                expt.add(entity);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        numTests = expt.size();

    }

    public int getNumberOfTests() {
        return numTests;
    }

    public String getURL() {
        if (counter < numTests) {
            String url = expt.get(counter).getURL();
            counter = counter + 1;
            return url;
        }

        return "null";
    }

    public ExperimentEntity getCurrentExpt() {
        if (counter <= numTests) {
            return expt.get(counter - 1);
        } else
            return null;
    }

}
