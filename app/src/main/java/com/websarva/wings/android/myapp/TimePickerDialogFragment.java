package com.websarva.wings.android.myapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private Calendar calendar;
    private AppCompatTextView textView;
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        if(calendar == null){
            calendar = Calendar.getInstance();
        }
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hourOfDay, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
        if(textView != null){
            String string = hourOfDay + ":" + minute;
            textView.setText(string);
        }
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
    }

    public Calendar getCalendar(){
        return calendar;
    }

    public void setTextView(AppCompatTextView textView){
        this.textView = textView;
    }
}
