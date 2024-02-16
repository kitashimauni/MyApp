package com.websarva.wings.android.myapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TimeTableManager {
    private TimeTableGridAdapter timeTableGridAdapter;
    private List<TimeTableItem> tableItemList;
    private final MainActivity activity;

    private static final String filepath = "timetable.json";

    public TimeTableManager(MainActivity activity){
        tableItemList = new ArrayList<>();
        this.activity = activity;
    }

    public void setTimeTableGridAdapter(Context context, int layout) {
        this.timeTableGridAdapter = new TimeTableGridAdapter(context, layout, this, activity);
    }

    public TimeTableGridAdapter getTimeTableGridAdapter() {
        return timeTableGridAdapter;
    }

    public List<TimeTableItem> getTableItemList() {
        return tableItemList;
    }

    public TimeTableItem getTableItem(int dayOfWeek, int period){
        for(TimeTableItem item : tableItemList){
            if(item.getDay_of_week() == dayOfWeek && item.getPeriod() == period){
                return item;
            }
        }
        return null;
    }

    public void addItem(TimeTableItem item){
        if(!tableItemList.contains(item)){
            tableItemList.add(item);
        }
    }

    public void deleteItem(int dayOfWeek, int period){
        tableItemList.removeIf(item -> (item.getDay_of_week() == dayOfWeek && item.getPeriod() == period));
    }

    public void notifyDataSetChanged(){
        timeTableGridAdapter.setView();
    }

    public void loadItemsFromFile(Context context){
        File file = new File(context.getFilesDir(), filepath);
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            String json = stringBuilder.toString();

            Gson gson = new Gson();
            Type type = new TypeToken<List<TimeTableItem>>() {}.getType();

            tableItemList = gson.fromJson(json, type);

            bufferedReader.close();
        } catch (NullPointerException e){
            Log.e("Error", "Null Pointer Exception in " + this);
        } catch (FileNotFoundException e){
            Log.e("Error", "File Not Found in " + this);
        } catch (IOException e){
            Log.e("Error", "IOException in " + this);
        }
    }

    public void saveItemsToFile(Context context){
        Gson gson = new Gson();
        String json = gson.toJson(tableItemList);
        File file = new File(context.getFilesDir(), filepath);

        try(FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write(json);
        } catch (IOException e){
            Log.e("Error", "IOException in " + this);
        }
    }

}
