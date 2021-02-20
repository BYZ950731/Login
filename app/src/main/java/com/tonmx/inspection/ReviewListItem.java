package com.tonmx.inspection;

class ReviewListItem {
    private int id;
    private String username;
    private String reviewform;
    private String reviewname;
    private String reviewtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReviewform() {
        return reviewform;
    }

    public void setReviewform(String reviewform) {
        this.reviewform = reviewform;
    }

    public String getReviewname() {
        return reviewname;
    }

    public void setReviewname(String reviewname) {
        this.reviewname = reviewname;
    }

    public String getReviewtime() {
        return reviewtime;
    }

    public void setReviewtime(String reviewtime) {
        this.reviewtime = reviewtime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

}
