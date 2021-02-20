package com.tonmx.inspection;

public class Todo {
    private int id;
    private String sheetName;
    private String fileName;
    private String updateDatetime;
    private String createDatetime;
    private int state;
    private int sheetLogId;
    private String inspector;

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
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

    public void setSheetLogId(int sheetLogId) {
        this.sheetLogId = sheetLogId;
    }
    public int getSheetLogId() {
        return sheetLogId;
    }
}
