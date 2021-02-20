package com.tonmx.inspection;

public class FormTempleteItem {
    private int id;
    private int sheetId;
    private int number;
    private String paging;
    private String deviceSeat;
    private String deviceName;
    private String review;
    private int type;
    private String createTime;
    private int click;
    private String remarks;
    private String status;
    private int userid;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setSheetId(int sheetId) {
        this.sheetId = sheetId;
    }
    public int getSheetId() {
        return sheetId;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setPaging(String paging) {
        this.paging = paging;
    }
    public String getPaging() {
        return paging;
    }

    public void setDeviceSeat(String deviceSeat) {
        this.deviceSeat = deviceSeat;
    }
    public String getDeviceSeat() {
        return deviceSeat;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceName() {
        return deviceName;
    }

    public void setReview(String review) {
        this.review = review;
    }
    public String getReview() {
        return review;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
