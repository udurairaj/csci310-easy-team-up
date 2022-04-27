package com.example.easyteamup;

import com.example.easyteamup.ui.MainActivity;

import java.util.TimerTask;

public class TimeGenerator extends TimerTask {
    Event event;

    public TimeGenerator() {

    }

    public TimeGenerator(Event event) {
        this.event = event;
    }

    @Override
    public void run() {
        if (event != null) {
            event = MainActivity.eventTable.getEvent(event.getEventID());
            event.generateFinalTime();
            event.getNotificationHandler().sendDueTimeNotif(event);
        }
    }
}
