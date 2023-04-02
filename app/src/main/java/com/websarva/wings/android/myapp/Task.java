package com.websarva.wings.android.myapp;

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
    @ColumnInfo(name = "dead_line")
    public Calendar dead_line;

    @ColumnInfo(name = "finished")
    public boolean finished;
}
