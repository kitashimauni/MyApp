package com.websarva.wings.android.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TaskListAdapter extends ArrayAdapter {

    private int _layout;
    private List<Task> _tasks;
    private LayoutInflater _inflater;
    public TaskListAdapter(Context context, int layout, List<Task> tasks){
        super(context, layout, tasks);

        _layout = layout;
        _tasks = tasks;
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        if(convertView != null){
            view = convertView;
        }else{
            view = _inflater.inflate(_layout, null);
        }

        Task item = _tasks.get(position);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(item.task_name);

        return view;
    }
}
