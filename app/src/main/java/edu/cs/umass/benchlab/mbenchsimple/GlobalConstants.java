package edu.cs.umass.benchlab.mbenchsimple;

public class GlobalConstants {

    private static String globalDirectoryName;
    private static String benchlabDirName;
    private static String experimentName;
    private static String constantDeviceID;
    private static GlobalConstants instance;

    //Constant String values
    public static final String WEBAPP_PATH = "/webapp";
    public static final String LOG_TAG = "benchlab";
    public static final String PATH_UPLOADRESULTS = WEBAPP_PATH + "/results/upload_results";
    public static final String PATH_UPLOAD_METADATA = WEBAPP_PATH + "/results/upload_results_metadata";
    public static final String PATH_REGISTER = WEBAPP_PATH + "/registerdevice/doregister";
    public static final String PATH_KEY = WEBAPP_PATH + "/registerdevice/upload_key";
    public static final String PATH_GET_TRACE = WEBAPP_PATH + "/tracemanagement/get_latest_trace";
    //public static final String IP_WEBAPP = "10.0.0.29:9998";
    public static final String IP_WEBAPP = "10.3.40.96:9998";


    public static GlobalConstants getInstance() {
        if (instance == null) {
            instance = new GlobalConstants();
        }
        return instance;
    }

    public static void setDirName(String name) {
        globalDirectoryName = name;
    }

    public static void setBenchlabDirName(String name) {
        benchlabDirName = name;
    }

    public static void setExperimentName(String name) {
        experimentName = name;
    }

    public static void setConstantDeviceID(String name) { constantDeviceID = name;}

    public static String getExperimentName() {
        return experimentName;
    }

    public static String getDirName() {
        return globalDirectoryName;
    }

    public static String getBenchlabDirName() {
        return benchlabDirName;
    }

    public static String getConstantDeviceID() { return constantDeviceID;}


}
