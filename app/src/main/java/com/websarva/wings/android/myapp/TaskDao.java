package com.websarva.wings.android.myapp;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Calendar;
import java.util.List;
import java.util.Date;

@Dao
public interface TaskDao{
    @Insert
    void insertAll(Task... task);
    @Update
    void updateTasks(Task... task);
    @Delete
    void deleteTasks(Task... task);
    @Query("SELECT * FROM task")
    List<Task> getAllTasks();
    @Query("SELECT * FROM task WHERE create_at < :calendar")
    List<Task> getAfter(Calendar calendar);
    @Query("SELECT * FROM task WHERE finished == :bool")
    List<Task> getAllTasksFinished(boolean bool);
    @Query("SELECT * FROM task WHERE finished == :bool ORDER BY dead_line")
    List<Task> getAllTasksFinishedOrder(boolean bool);
}