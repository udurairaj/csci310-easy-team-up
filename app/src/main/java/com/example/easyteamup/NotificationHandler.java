package com.example.easyteamup;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

public class NotificationHandler implements Serializable {

    private NotificationChannel channel = null;
    private NotificationManager manager = null;

    public NotificationHandler(Context context) {
        channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID,
                "notifications", NotificationManager.IMPORTANCE_DEFAULT);
        manager = (NotificationManager) context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    public NotificationChannel getChannel() { return channel; }

    public void notify(Context context, Intent intent, Event event, int type) {
        if (type == 0) {
            Notification n = new Notification
                    (event, event.getOwner(), 0, intent, context, this);
            n.send(context);
        }
        else if (type == 1) {
            Notification n = new Notification
                    (event, event.getOwner(), 1, intent, context,this);
            n.send(context);
            for (User u : event.getParticipants()) {
                Notification notif = new Notification
                        (event, u, 1, intent, context,this);
                notif.send(context);
            }
        }
        else if (type == 2) {
            for (User u : event.getParticipants()) {
                Notification notif = new Notification
                        (event, u, 2, intent, context,this);
                notif.send(context);
            }
        }
        else if (type == 3) {
            Notification n = new Notification
                    (event, event.getOwner(), 3, intent, context,this);
            n.send(context);
        }
    }

}
