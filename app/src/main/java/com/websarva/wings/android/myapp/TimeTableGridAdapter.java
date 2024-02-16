package com.websarva.wings.android.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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

    public void initView(Fragment fragment, GridLayout gridLayout){
        TimeTableItem tableItem;
        for(int dayOfWeek = 0; dayOfWeek < 5; dayOfWeek++){
            for(int period = 0; period < 6; period++){
                ViewHolder holder = new ViewHolder();
                View view = inflater.inflate(layout, gridLayout, false);

                holder.name = view.findViewById(R.id.table_item_name);
                holder.place = view.findViewById(R.id.table_item_place);
                holder.dayOfWeek =  dayOfWeek;
                holder.period = period;
                holder.length = 1;

                if((tableItem = timeTableManager.getTableItem(dayOfWeek, period)) == null){
                    holder.name.setText("");
                    holder.place.setText("");
                } else {
                    holder.name.setText(tableItem.getSubject_name());
                    holder.place.setText(tableItem.getPlace());
                }

                view.setTag(holder);

                view.setBackgroundColor(view.getResources().getColor(
                        ((dayOfWeek + period) % 2 == 0 ? R.color.background1 : R.color.background2), activity.getTheme()));
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.columnSpec = GridLayout.spec(holder.dayOfWeek, 1, 1);
                layoutParams.rowSpec = GridLayout.spec(holder.period, 1, 1);
                view.setLayoutParams(layoutParams);
                gridLayout.addView(view);

                tableItemViewList.add(view);

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ViewHolder holder = (ViewHolder) v.getTag();
                        NoticeDialogFragment noticeDialogFragment = new NoticeDialogFragment();
                        noticeDialogFragment.setMessage("編集しますか?\n"+
                                "(" + TimeTableItem.int_to_day_of_week(v.getContext(), holder.dayOfWeek) + "曜日" +
                                (holder.period + 1) + "限目)");
                        Bundle data = new Bundle();
                        data.putInt("dayOfWeek", holder.dayOfWeek);
                        data.putInt("period", holder.period);
                        noticeDialogFragment.setData(data);
                        noticeDialogFragment.show(fragment.getChildFragmentManager(), "Notice_Edit_TimeTable");
                        return false;
                    }
                });
            }
        }
        gridLayout.requestLayout();
    }

    public void setView() {
        for(int dayOfWeek = 0; dayOfWeek < 5; dayOfWeek++){
            for(int period = 0; period < 6; period++){
                View view = getTableItemView(dayOfWeek, period);
                if(view == null) {
                    Log.e("Error", "view is null in" + this + "setView");
                    return;
                }
                ViewHolder holder = (ViewHolder) view.getTag();
                TimeTableItem tableItem = timeTableManager.getTableItem(dayOfWeek, period);
                if(tableItem == null){
                    holder.name.setText("");
                    holder.place.setText("");
                } else {
                    holder.name.setText(tableItem.getSubject_name());
                    holder.place.setText(tableItem.getPlace());
                    holder.length = tableItem.getLength();
                }
            }
        }
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
