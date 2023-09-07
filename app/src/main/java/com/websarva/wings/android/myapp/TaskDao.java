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

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface TaskDao{
    @Insert
    public Completable insertAll(Task... task);
    @Update
    public Completable updateTasks(Task... task);
    @Delete
    public Completable deleteTasks(Task... task);
    @Query("SELECT * FROM task")
    Single<List<Task>> getAllTasks();
    @Query("SELECT * FROM task WHERE create_at < :calendar")
    Single<List<Task>> getAfter(Calendar calendar);
    @Query("SELECT * FROM task WHERE finished == :bool")
    Single<List<Task>> getAllTasksFinished(boolean bool);
    @Query("SELECT * FROM task WHERE finished == :bool ORDER BY dead_line")
    Single<List<Task>> getAllTasksFinishedOrder(boolean bool);
}