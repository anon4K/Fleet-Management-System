package com.tipsycoder.helper;

/**
 * Created by TipsyCoder on 2/8/15.
 */
public class ErrorHelper {
    private boolean error;
    private String message;

    public ErrorHelper() {
        setError(false);
        setMessage("");
    }


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
