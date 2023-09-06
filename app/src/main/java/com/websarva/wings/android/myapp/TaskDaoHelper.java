package com.websarva.wings.android.myapp;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDaoHelper {
    public List<Task> taskList;
    public boolean AddTask(Task task, TaskDao taskDao){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    taskDao.insertAll(task);
                }catch (Exception e){
                    Log.e("Error", "Databaseへの追加に失敗");
                }
            }
        });
        return true;
    }

    public List<Task> GetTaskAscendingOrder(TaskDao taskDao){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    taskList = taskDao.getAllTasksFinishedOrder(false);
                }catch (Exception e){
                    Log.e("Error", "DataBaseからの情報取得に失敗");
                    Log.e("Error", e.getMessage());
                }
                if(taskList.isEmpty())
                    Log.d("Error", "取得に失敗");
            }
        });
        while (taskList == null){
            try {
                wait(10);
            }catch (Exception e){
                continue;
            }
        }
        return taskList;
    }
}
