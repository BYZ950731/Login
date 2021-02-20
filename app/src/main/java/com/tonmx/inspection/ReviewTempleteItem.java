package com.tonmx.inspection;

public class ReviewTempleteItem {

    private int id;
    private int sheetid;
    private int sheetLogId;
    private String deviceSeat;
    private String deviceName;
    private String review;
    private String status;
    private long createDatetime;
    private int number;
    private int type;
    private String paging;
    private  String remarks;

    public int getSheetid() {
        return sheetid;
    }

    public void setSheetid(int sheetid) {
        this.sheetid = sheetid;
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

    public void setSheetLogId(int sheetLogId) {
        this.sheetLogId = sheetLogId;
    }
    public int getSheetLogId() {
        return sheetLogId;
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

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }
    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setPaging(String paging) {
        this.paging = paging;
    }
    public String getPaging() {
        return paging;
    }

}
