package com.tonmx.inspection;

public class Hand {
    private int id;
    private String sheetName;
    private String fileName;
    private String updateDatetime;
    private String createDatetime;
    private int sheetLogId;
    private int state;
    private String reviewer;
    private int rid;

    public int getrId() {
        return rid;
    }

    public void setrId(int rId) {
        this.rid = rId;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public int getSheetLogId() {
        return sheetLogId;
    }

    public void setSheetLogId(int sheetLogId) {
        this.sheetLogId = sheetLogId;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
    public String getSheetName() {
        return sheetName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return fileName;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }
    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }
}
