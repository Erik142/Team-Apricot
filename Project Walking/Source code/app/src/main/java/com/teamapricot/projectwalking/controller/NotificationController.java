package com.teamapricot.projectwalking.controller;

import android.content.Context;
import com.teamapricot.projectwalking.model.Reminder;
import com.teamapricot.projectwalking.model.Model;
import com.teamapricot.projectwalking.model.Route;
import com.teamapricot.projectwalking.model.User;
import java.util.ArrayList;
import java.lang.*;


/**
 * @author Valeria Nafuna
 * @version 2021-09-29
 * NotificationController controller class for sending notifications.
 */
public class NotificationController {
    private final String NotificationTitle="new_spot";
    private final String NotificationMessage="new_challenge";
    private final String channel_id="notify_message";
    private final int notificationRequestCode = 1;

    private final String userId = "1";
    private final ArrayList<Route> routes = new ArrayList<Route>();
    private Reminder Reminder;
    private Model Model;
    private User NewUser;

    /**
     * creates a new NotificationController
     * @param context used to access android system application
     */
    public NotificationController(Context context){
        this.Reminder=new Reminder(context);
        Reminder.createChannel();
        this.Model=new Model();
        NewUser=new User(userId,routes);

    }

    /**
     *sends a notification to the user
     * @param check_time used to check the users activity.
     */
    public void SendNotification(boolean check_time ) {
        if ((NewUser.getRoutes().size() > 0 && Model.checkRouteTime(NewUser)) || !check_time) {
            Reminder.addNotification(NotificationTitle, NotificationMessage, channel_id, notificationRequestCode);
        }
    }

    /**
     * test notifications function .since we don't have any routes yet
     */
    public void SendNotification() {

        SendNotification(true);
    }
}

