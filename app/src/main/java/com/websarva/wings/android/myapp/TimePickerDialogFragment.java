package com.websarva.wings.android.myapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
    }
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
        setText(hourOfDay, minute);
    }

    public void setText(int hourOfDay, int minute){
        if(textView != null){
            String string = (hourOfDay < 10 ? ("0" + hourOfDay) : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute);
            textView.setText(string);
        }
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
        setText(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public void setTextView(AppCompatTextView textView){
        this.textView = textView;
    }
}
