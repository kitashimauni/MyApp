package com.websarva.wings.android.myapp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class TimeTableManager {
    private List<TimeTableItem> tableItemList;

    public void loadItemFromFile(){
        try (FileReader fileReader = new FileReader("timetable.json")) {
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

}
