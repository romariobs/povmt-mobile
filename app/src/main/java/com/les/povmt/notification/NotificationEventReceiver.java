package com.les.povmt.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by felipe on 12/11/16.
 */

/*
 * UNNUSED
 */
public class NotificationEventReceiver extends WakefulBroadcastReceiver {

    private static final String POVMT_PREFS = "POVMT_PREFS";
    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";
    private static final int TWENTY_HOURS_IN_MILI = 86400000;


    public static void setupAlarm(Context context, Intent intent) {

        SharedPreferences prefs = context.getSharedPreferences(POVMT_PREFS, context.MODE_PRIVATE);
        int timeset_hour = prefs.getInt("TimeScheduleHour", 00);
        int timeset_min = prefs.getInt("TimeScheduleMin", 00);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);


        long time = intent.getLongExtra("time", Integer.MAX_VALUE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                getTimeMilliNotification(context),
                TWENTY_HOURS_IN_MILI, //24 hours //THIENGO CALOPSITA - NOTIFICAÇÃO
                alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("FUNFOU", "CHAMA QUE É NOIS");
        String action = intent.getAction();
        Intent serviceIntent = null;

        if ("NOTIFY_TI".equals(action)) {

            serviceIntent = NotificationIntentService.createIntentStartNotificationService(context);
        } else if (ACTION_DELETE_NOTIFICATION.equals(action)) {
            serviceIntent = NotificationIntentService.createIntentDeleteNotification(context);
        }

        if (serviceIntent != null) {
            startWakefulService(context, serviceIntent);
        }
    }

    public static long getTimeMilliNotification(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(POVMT_PREFS, context.MODE_PRIVATE);
        int timeset_hour = prefs.getInt("TimeScheduleHour", 00);
        int timeset_min = prefs.getInt("TimeScheduleMin", 00);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timeset_hour);
        calendar.set(Calendar.MINUTE, timeset_min);
        calendar.set(Calendar.SECOND, 00);

        return calendar.getTimeInMillis();


    }

    private static long getTriggerAt(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        return calendar.getTimeInMillis();
    }

    private static PendingIntent getStartPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        intent.setAction(ACTION_DELETE_NOTIFICATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
