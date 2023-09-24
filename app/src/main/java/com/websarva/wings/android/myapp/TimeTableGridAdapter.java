package com.websarva.wings.android.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class TimeTableGridAdapter extends BaseAdapter {
    private final int layout;
    private final TimeTableManager timeTableManager;
    private final LayoutInflater inflater;
    public TimeTableGridAdapter(Context context, int layout, TimeTableManager timeTableManager){
        this.layout = layout;
        this.timeTableManager = timeTableManager;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        return null;
    }

    @Override
    public int getCount() {
        // return tableItemList.size();
        return 30;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
