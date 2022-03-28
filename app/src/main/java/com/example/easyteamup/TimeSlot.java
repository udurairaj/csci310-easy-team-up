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
    private Calendar cal = null;

    // constructor
    public TimeSlot(String datetime)
    {
        month = Integer.parseInt(datetime.substring(0, 2));
        day = Integer.parseInt(datetime.substring(3, 5));
        year = Integer.parseInt(datetime.substring(6, 10));
        hour = Integer.parseInt(datetime.substring(11, 13));
        minute = Integer.parseInt(datetime.substring(15));
        this.cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        Log.d("datetime", cal.getTime().toString());
    }

    public Date getDateTime() {
        return cal.getTime();
    }

    public String getDateTimeToString()
    {
        return cal.getTime().toString();
    }


}
