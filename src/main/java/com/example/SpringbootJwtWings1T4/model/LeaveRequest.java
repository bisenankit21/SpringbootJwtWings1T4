package com.example.SpringbootJwtWings1T4.model;

import java.util.Date;

public class LeaveRequest {
    private String category;
    private int noOfDays;
    private Date applied_on;
    private String description;


    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getApplied_on() {
        return applied_on;
    }

    public void setApplied_on(Date applied_on) {
        this.applied_on = applied_on;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
