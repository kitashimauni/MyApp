package com.websarva.wings.android.myapp;

public class TimeTableItem {
    private String subject_name;
    private int day_of_week;
    private int period;
    private String place;

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public int getPeriod() {
        return period;
    }

    public String getPlace() {
        return place;
    }
}
