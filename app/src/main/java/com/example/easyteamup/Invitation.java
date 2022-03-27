package com.example.easyteamup;

public class Invitation {

    private User user;
    private Event event;

    public Invitation(User user, Event event) {
        this.user = user;
        this.event = event;
    }

    public void changeState(int state) {
        // Feature 3
    }
}
