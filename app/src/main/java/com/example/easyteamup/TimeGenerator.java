package com.example.easyteamup;

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
            event.generateFinalTime();
            event.getNotificationHandler().sendDueTimeNotif(event);
        }
    }
}
