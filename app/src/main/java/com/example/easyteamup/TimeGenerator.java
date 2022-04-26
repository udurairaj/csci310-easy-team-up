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
<<<<<<< HEAD
            event.getNotificationHandler().sendDueTimeNotif();
=======
            event.getNotificationHandler().sendDueTimeNotif(event);
>>>>>>> 6f769013197e21da5c8b0e8c1775cf8224d725a4
        }
    }
}
