package com.tonmx.inspection;

public class ReviewerDialogItem {
    private boolean isSelected = false;
    private String text;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ReviewerDialogItem(String text,int id, boolean isSelected) {
        this.text = text;
        this.id = id;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
