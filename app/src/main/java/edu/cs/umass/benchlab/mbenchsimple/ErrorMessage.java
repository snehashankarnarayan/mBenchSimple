package edu.cs.umass.benchlab.mbenchsimple;

/**
 * Created by snehas on 3/2/15.
 */
public class ErrorMessage {
    public enum Status {
        SUCCESS, FAILURE;
    }

    private Status status;
    private String message;

    public ErrorMessage() {
        status = Status.FAILURE;
    }

    public ErrorMessage(Status status, String message) {
        this.message = message;
        this.status = status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean getStatusBoolean() {
        if (status == Status.SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
