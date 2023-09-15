package com.websarva.wings.android.myapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private String string_year;
    private String string_month;
    private String string_dayOfMonth;
    private Calendar calendar;
    private AppCompatTextView textView;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        if(calendar == null){
            calendar = Calendar.getInstance();
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        calendar.set(year, month, day);
        setText(year, month, day);
    }

    private void setText(int year, int month, int day){
        if(textView != null){
            String string = year + string_year + (month + 1) + string_month + day + string_dayOfMonth ;
            textView.setText(string);
        }
    }

    public void setString(String string_year, String string_month, String string_dayOfMonth) {
        this.string_year = string_year;
        this.string_month = string_month;
        this.string_dayOfMonth = string_dayOfMonth;
    }

    public void setCalender(Calendar calendar){
        this.calendar = calendar;
        setText(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }
    public void setTextView(AppCompatTextView textView){
        this.textView = textView;
    }
}
