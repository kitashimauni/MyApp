package com.websarva.wings.android.myapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TasksViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TasksViewHolder(View itemView){
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.task_list_title);
    }
}
