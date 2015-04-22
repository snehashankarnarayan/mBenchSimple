package edu.cs.umass.benchlab.mbenchsimple;

import java.util.LinkedList;

import android.util.Log;

public class ExperimentResults {

    private static ExperimentResults instance = null;
    private static PhoneExperimentData phoneData;
    private static LinkedList<ResultEntity> results;
    private static double magicNumber = 0;

    public static ExperimentResults getInstance() {
        if (instance == null) {
            instance = new ExperimentResults();
        }
        return instance;
    }

    ExperimentResults() {
        ExperimentList inst = ExperimentList.getInstance();
        results = new LinkedList<ResultEntity>();
        phoneData = new PhoneExperimentData();
    }

    public static void print() {
        for (ResultEntity res : results) {
            Log.d("benchlab", "printing " + res.getTag());
        }
    }

    public static void updateResults(ResultEntity res) {
        results.add(res);
        Log.d("benchlab", "printing size " + results.size());
    }

    public static void updatePhoneData(PhoneExperimentData data) {
        phoneData = data;
    }

    public static PhoneExperimentData getPhoneData() { return phoneData;}

    public LinkedList<ResultEntity> getResults() {
        return results;
    }

    public static double getMagicNumber() { return  magicNumber;}

    public static void setMagicNumber(double mn) { magicNumber = mn; }


}
