package com.websarva.wings.android.myapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TimeTableGridAdapter {

    static class ViewHolder{
        int dayOfWeek;
        int period;
        int length;
        TextView name;
        TextView place;
    }

    private final int layout;
    private final TimeTableManager timeTableManager;
    private List<View> tableItemViewList;
    private final MainActivity activity;
    private final LayoutInflater inflater;
    public TimeTableGridAdapter(Context context, int layout, TimeTableManager timeTableManager, MainActivity activity){
        this.layout = layout;
        this.timeTableManager = timeTableManager;
        this.activity = activity;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tableItemViewList = new ArrayList<>();
    }

    public void initView(GridLayout gridLayout){
        for(int dayOfWeek = 0; dayOfWeek < 5; dayOfWeek++){
            for(int period = 0; period < 6; period++){
                ViewHolder holder = new ViewHolder();
                View view = inflater.inflate(layout, gridLayout, false);

                holder.name = view.findViewById(R.id.table_item_name);
                holder.name.setText("テスト～");
                holder.place = view.findViewById(R.id.table_item_place);
                holder.place.setText("場所だよ～");
                holder.dayOfWeek =  dayOfWeek;
                holder.period = period;
                holder.length = 1;

                view.setTag(holder);

                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.columnSpec = GridLayout.spec(holder.dayOfWeek, 1, 1);
                layoutParams.rowSpec = GridLayout.spec(holder.period, 1, 1);
                view.setLayoutParams(layoutParams);
                gridLayout.addView(view);

                tableItemViewList.add(view);

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
            }
        }
        gridLayout.requestLayout();
    }

    public void setView() {
        List<TimeTableItem> itemList = timeTableManager.getTableItemList();
        for(TimeTableItem item : itemList){
            View view = getTableItemView(item.getDay_of_week(), item.getPeriod());
            if(view == null) {
                Log.e("Error", "view is null in" + this + "setView");
                return;
            }
            ViewHolder holder = (ViewHolder) view.getTag();


        }
    }

    public void notifyDataSetChanged(){

    }

    private View getTableItemView(int dayOfWeek, int period){
        for(View item : tableItemViewList){
            ViewHolder holder = (ViewHolder) item.getTag();
            if(holder.dayOfWeek == dayOfWeek && holder.period == period){
                return item;
            }
        }
        return null;
    }
}
