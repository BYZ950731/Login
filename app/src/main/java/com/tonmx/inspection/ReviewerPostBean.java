package com.tonmx.inspection;

import java.util.List;

class ReviewerPostBean {
    private int id;
    private int userId;
    private String sheetName;
    private String inspector;
    private String reviewer;
    private String executeState;
    private int sheetLogId;
    private int rid;

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getSheetLogId() {
        return sheetLogId;
    }

    public void setSheetLogId(int sheetLogId) {
        this.sheetLogId = sheetLogId;
    }

    private List<FormTempleteItem> templateTableList;
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

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }
    public String getInspector() {
        return inspector;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }
    public String getReviewer() {
        return reviewer;
    }

    public void setExecuteState(String executeState) {
        this.executeState = executeState;
    }
    public String getExecuteState() {
        return executeState;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }

    public void setTemplateTableList(List<FormTempleteItem> templateTableList) {
        this.templateTableList = templateTableList;
    }
    public List<FormTempleteItem> getTemplateTableList() {
        return templateTableList;
    }
}
