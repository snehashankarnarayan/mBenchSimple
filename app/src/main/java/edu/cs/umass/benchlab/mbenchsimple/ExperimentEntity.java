package edu.cs.umass.benchlab.mbenchsimple;

public class ExperimentEntity {

    private String url;
    private String tag;
    private String exptID;

    ExperimentEntity() {
        exptID = "expt";
    }

    ExperimentEntity(String tag, String url) {
        setEntity(tag, url);
    }

    public void setEntity(String tag, String url) {
        this.url = url;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getURL() {
        return url;
    }

}
