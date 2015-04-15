package edu.cs.umass.benchlab.mbenchsimple;

public class ResultEntity {

    private String url;
    private String tag;
    private long start;
    private long end;
    private long latency;
    private long bytes;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
        this.latency = (this.end - this.start);
    }

    public void setBytes(long b) { this.bytes = b; }

    public long getBytes() { return bytes; }

    public String getURL() {
        return this.url;
    }

    public String getTag() {
        return this.tag;
    }

    public long getLatency() {
        return this.latency;
    }

}
