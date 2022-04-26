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
        event.generateFinalTime();
        event.getNotificationHandler().sendDueTimeNotif();
    }
}
