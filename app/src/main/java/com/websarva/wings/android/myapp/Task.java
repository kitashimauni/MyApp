package com.websarva.wings.android.myapp;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "task_name")
    public String task_name;
    @ColumnInfo(name = "task_detail")
    public String task_detail;

    @ColumnInfo(name = "create_at")
    public Calendar create_at;
    @ColumnInfo(name = "updated_at")
    public Calendar updated_at;
    @Nullable
    @ColumnInfo(name = "dead_line")
    public Calendar dead_line;
    @ColumnInfo(name = "has_deadline")
    public boolean has_deadline;
    @ColumnInfo(name = "all_day")
    public boolean all_day;

    @ColumnInfo(name = "finished")
    public boolean finished = false;

    /*
    public Task(String task_name, String task_detail, Calendar create_at, Calendar updated_at, Calendar dead_line, boolean finished){
        this.task_name = task_name;
        this.task_detail = task_detail;
        this.create_at = create_at;
        this.updated_at = create_at;
        this.dead_line = dead_line;
        this.finished = finished;
    }
    */

    public String getTask_name() {
        return task_name;
    }

    public String getTask_detail() {
        return task_detail;
    }

    public Calendar getDead_line() {
        return dead_line;
    }

    public boolean isAll_day() {
        return all_day;
    }

    public boolean has_deadline(){
        return has_deadline;
    }
}
