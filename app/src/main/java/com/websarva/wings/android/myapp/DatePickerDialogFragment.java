package com.websarva.wings.android.myapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    // private int year = -1;
    // private int month;
    // private int dayOfMonth;
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
        if(textView != null){
            String string = year + getString(R.string.year) + (month + 1) + getString(R.string.month) + day + getString(R.string.day) ;
            textView.setText(string);
        }
    }

    public void setCalender(Calendar calendar){
        this.calendar = calendar;
    }
    public Calendar getCalendar(){
        return calendar;
    }
    public void setAppCompatTextView(AppCompatTextView textView){
        this.textView = textView;
    }
}
