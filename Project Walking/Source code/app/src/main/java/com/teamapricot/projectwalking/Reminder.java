package com.teamapricot.projectwalking;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Reminder {
    private Context ctx;

    public Reminder(Context mctx) {
        this.ctx=mctx;
    }


    //building the notification text and drop down arrow
    public void addNotification( String notificationTitle, String notificationMessage,String channel_id, int notificationRequestCode) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this.ctx.getApplicationContext(),channel_id);;
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationMessage);
        builder.setSmallIcon(android.R.drawable.arrow_down_float);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat;
        managerCompat = NotificationManagerCompat.from(this.ctx);
        managerCompat.notify(notificationRequestCode, builder.build());

    }
}




