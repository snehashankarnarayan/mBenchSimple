package edu.cs.umass.benchlab.mbenchsimple;

/**
 * Created by snehas on 2/27/15.
 */
public class PhoneExperimentData {

    private String version;
    private String location;
    private String ipAddress;

    public void setVersion(String data) {
        this.version = data;
    }

    public void setLocation(String data) {
        this.location = data;
    }

    public void setIpAddress(String data) {
        this.ipAddress = data;
    }

    public String getVersion()
    {
        return this.version;
    }

    public String getLocation()
    {
        return this.location;
    }

    public String getIpAddress()
    {
        return this.ipAddress;
    }
}
