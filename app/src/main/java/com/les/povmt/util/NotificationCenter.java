package com.les.povmt.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.les.povmt.R;
import com.les.povmt.notification.ItService;
import com.les.povmt.notification.ResultActivity;

/**
 * Created by romariobs on 07/12/16.
 */

public class NotificationCenter {

    private final Activity mActivity;

    public NotificationCenter (Activity activity) {
        mActivity = activity;

    }


    ///////////////////// notification center //////////////////////
    ///////////////////////////////////////////////////////////////

    /**
     * Show a notification with no action.
     * @param notiTitle Notification title.
     * @param notiDescription Notification description.
     */
    public void simpleNotification(String notiTitle, String notiDescription) {
        int notificationId = 0;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mActivity)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notiTitle)
                        .setContentText(notiDescription);

        NotificationManager mNotificationManager =
                (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    /**
     * Show a notification with action when click.
     * @param notiTitle Title of notification
     * @param notiDescription Description of notification
     * @param notiActiDest Activity destination
     */
    public void withActionNotification(String notiTitle, String notiDescription, Activity notiActiDest) {
        int notificationId = 1;

        Intent resultIntent = new Intent(mActivity, ResultActivity.class);
        int requestCode = 0; //Private request code for the sender
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(mActivity, requestCode, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mActivity)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Action notification")
                        .setContentText("Oi, eu tenho um resultado :)")
                        .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }


    @SuppressLint("NewApi")
    public void setNotificationRemoteView() {
        mActivity.startService(new Intent(mActivity, ItService.class));
    }

    public void deleteNotification() {
        mActivity.stopService(new Intent(mActivity, ItService.class));

        final NotificationManager notificationManager =
                (NotificationManager) mActivity.getSystemService(
                        Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
