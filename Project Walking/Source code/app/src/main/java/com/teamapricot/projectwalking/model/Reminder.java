package com.teamapricot.projectwalking.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * @author Valeria Nafuna
 * @version 2021-09-22
 *
 * Reminder is used to create notification
 */
public class Reminder {
    private Context ctx;

    /**
     *create new Reminder for passing the context.
     * @param mctx  used to access android system application.
     *
     */
    public Reminder(Context mctx) {
        this.ctx=mctx;
    }


    /**
     *
     * @param notificationTitle used for title os notification
     * @param notificationMessage used to deliver a message to the user
     * @param channel_id create channel id
     * @param notificationRequestCode passes number notify
     *
     *   create notification constructor that is used to pass on the contents to be notified.
     *   building the notification text and drop down arrow
     */
    public void addNotification( String notificationTitle, String notificationMessage,String channel_id, int notificationRequestCode) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this.ctx.getApplicationContext(),channel_id);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationMessage);
        builder.setSmallIcon(android.R.drawable.arrow_down_float);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat;
        managerCompat = NotificationManagerCompat.from(this.ctx);
        managerCompat.notify(notificationRequestCode, builder.build());

    }

    public void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_message", "new_spot", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = ctx.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}








