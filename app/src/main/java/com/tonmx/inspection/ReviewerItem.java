package com.tonmx.inspection;

public class ReviewerItem {
    private int id;
    private String username;
    private String fullName;
    private int departmentId;
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getFullName() {
        return fullName;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    public int getDepartmentId() {
        return departmentId;
    }
}
