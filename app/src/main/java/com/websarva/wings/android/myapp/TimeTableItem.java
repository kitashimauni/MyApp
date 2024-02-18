package com.websarva.wings.android.myapp;

import android.content.Context;

import java.io.Serializable;

public class TimeTableItem {
    private String subject_name;
    private int day_of_week;
    private int period;
    private String place;
    private int length;
    private String detail;

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

    public void setLength(int length) {
        this.length = length;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public int getLength() {
        return length;
    }

    public String getDetail() {
        return detail;
    }

    public static String int_to_day_of_week(Context context, int num){
        switch (num){
            case 0:
                return context.getString(R.string.monday);
            case 1:
                return context.getString(R.string.tuesday);
            case 2:
                return context.getString(R.string.wednesday);
            case 3:
                return context.getString(R.string.thursday);
            case 4:
                return context.getString(R.string.friday);
            default:
                return "";
        }
    }
}
