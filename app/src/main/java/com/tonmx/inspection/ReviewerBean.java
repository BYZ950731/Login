package com.tonmx.inspection;

import java.util.List;

public class ReviewerBean {
    private String result;
    private List<ReviewerItem> data;
    private int errorCode;
    private String errorMessage;
    public void setResult(String result) {
        this.result = result;
    }
    public String getResult() {
        return result;
    }

    public void setData(List<ReviewerItem> data) {
        this.data = data;
    }
    public List<ReviewerItem> getData() {
        return data;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
