package com.websarva.wings.android.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TimeTableGridAdapter extends BaseAdapter {

    static class ViewHolder{
        TextView name;
        TextView place;
    }

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
        ViewHolder holder;
        if(view == null){
            view = inflater.inflate(layout, viewGroup, false);
            holder = new ViewHolder();

            holder.name = view.findViewById(R.id.table_item_name);
            holder.place = view.findViewById(R.id.table_item_place);

            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(i);
        // holder.name.setText(timeTableManager.getTableItemList().get(i).getSubject_name());
        // holder.place.setText(timeTableManager.getTableItemList().get(i).getPlace());

        return view;
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
