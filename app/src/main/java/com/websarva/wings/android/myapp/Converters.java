package com.websarva.wings.android.myapp;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import java.sql.Date;
import java.util.Calendar;

public class Converters {
    @TypeConverter
    public static Calendar fromTimestamp(long value){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        return calendar;
    }
    @TypeConverter
    public static long calendarToTimestamp(Calendar calendar){
        return calendar == null ? null : calendar.getTimeInMillis();
    }
}
