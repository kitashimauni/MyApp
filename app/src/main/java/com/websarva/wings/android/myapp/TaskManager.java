package com.websarva.wings.android.myapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

// タスクのいろいろを管理する
public class TaskManager {
    private TaskListAdapter taskListAdapter;
    private ArrayList<Task> tasks;
    private TaskDao taskDao;
    private MainActivity activity;

    public TaskManager(MainActivity activity){
        tasks = new ArrayList<>();
        this.activity = activity;
    }

    public void setTaskDao(TaskDao taskDao){
        this.taskDao = taskDao;
    }

    public void setTaskListAdapter(Context context, int layout){
        taskListAdapter = new TaskListAdapter(context, layout, tasks);
    }

    public TaskListAdapter getTaskListAdapter(){
        return taskListAdapter;
    }

    public ArrayList<Task> getTasks(){
        return tasks;
    }

    public Task getTask(int position){
        return tasks.get(position);
    }

    public void loadTasks(){
        tasks.clear();
        Disposable disposable = taskDao.getAllTasksFinished(false) // データベースクエリを実行し、Singleを取得
                .subscribeOn(Schedulers.io()) // バックグラウンドスレッドで実行
                .observeOn(AndroidSchedulers.mainThread()) // メインスレッドで結果を処理
                .subscribe(
                        items -> {
                            // 成功時の処理: クエリからのタスクリストを使用
                            // tasksはList<Task>型で、クエリの結果が格納されています
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tasks.addAll(items);
                                    taskListAdapter.notifyDataSetChanged();
                                }
                            });
                        },
                        error -> {
                            // エラー時の処理: エラーハンドリングを行うことが重要です
                            // エラー情報を取得し、適切な対応を行います
                            Log.e("Fragment", error.getMessage());
                        }
                );
    }

    public void addTask(Task task){
        task.create_at = Calendar.getInstance();
        task.updated_at = Calendar.getInstance();
        try{
            Disposable disposable = taskDao.insertAll(task) // データベースクエリを実行し、Singleを取得
                .subscribeOn(Schedulers.io()) // バックグラウンドスレッドで実行
                .observeOn(AndroidSchedulers.mainThread()) // メインスレッドで結果を処理
                .subscribe(this::loadTasks);
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }
}
