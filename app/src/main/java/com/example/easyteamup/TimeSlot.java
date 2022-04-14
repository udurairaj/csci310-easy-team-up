package com.example.easyteamup;

import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class TimeSlot implements Serializable
{
    // data members
    private int month;
    private int day;
    private int year;
    private int hour;
    private int minute;
    private int duration = 0;
    private Calendar cal = null;

    public TimeSlot() {}

    public TimeSlot(String datetime) {
        month = Integer.parseInt(datetime.substring(0, 2));
        day = Integer.parseInt(datetime.substring(3, 5));
        year = Integer.parseInt(datetime.substring(6, 10));
        hour = Integer.parseInt(datetime.substring(11, 13));
        minute = Integer.parseInt(datetime.substring(14));
        this.duration = 0;
        this.cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        //Log.d("datetime", cal.getTime().toString());
    }

    // constructor
    public TimeSlot(String datetime, int duration)
    {
        month = Integer.parseInt(datetime.substring(0, 2));
        day = Integer.parseInt(datetime.substring(3, 5));
        year = Integer.parseInt(datetime.substring(6, 10));
        hour = Integer.parseInt(datetime.substring(11, 13));
        minute = Integer.parseInt(datetime.substring(14));
        this.duration = duration;
        this.cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        Log.d("datetime", cal.getTime().toString());
    }

    public int getMonth() { return month; }
    public int getDay() { return day; }
    public int getYear() { return year; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public int getDuration() { return duration; }

    public void setMonth(int month) { this.month = month; }
    public void setDay(int day) { this.day = day; }
    public void setYear(int year) { this.year = year; }
    public void setHour(int hour) { this.hour = hour; }
    public void setMinute(int minute) { this.minute = minute; }
    public void setDuration(int duration) { this.duration = duration; }

    public Date dateTimeAsDate() {
        if (cal == null) {
            cal = Calendar.getInstance();
            cal.set(year, month, day, hour, minute);
        }
        return cal.getTime();
    }

    public String toStringDateTime()
    {
        if (cal == null) {
            cal = Calendar.getInstance();
            cal.set(year, month, day, hour, minute);
        }
        return cal.getTime().toString();
    }


}
