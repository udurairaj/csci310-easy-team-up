//package com.example.easyteamup;
//
//import android.app.NotificationChannel;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//public class Notification {
//
//    private int recipient;
//    private Event event;
//    private String message;
//    private NotificationHandler notificationHandler;
//    private String[] typeToMessage = { "A new participant has joined your event, ",
//            " is scheduled for ", "Changes have been made to the event you joined, ",
//            "A participant has withdrawn from your event, " };
//    private NotificationCompat.Builder builder = null;
//
//    public Notification(Event event, int recipient, int type, Intent intent, Context context,
//                        NotificationHandler notificationHandler) {
//        this.recipient = recipient;
//        this.event = event;
//        if (type == 1) {
//           // this.message = event.getEventName() + typeToMessage[type] + event.getFinalTime();
//        }
//        else {
//            this.message = typeToMessage[type] + event.getEventName();
//        }
//        PendingIntent pendingIntent = PendingIntent.getActivity
//                (context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//        builder = new NotificationCompat.Builder
//                (context, NotificationChannel.DEFAULT_CHANNEL_ID)
//                //.setSmallIcon(R.drawable.app_icon)
//                .setContentTitle("Easy Team Up")
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//    }
//
//    public void send(Context context) {
//        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
//        nm.notify(0, builder.build());
//    }
//
//}
