package com.les.povmt.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.les.povmt.R;

/**
 * Created by romariobs on 05/12/16.
 */

public class ItService extends Service {

    private final String ACTION_PLAY = "com.les.povmt.ACTION_PLAY";

    private RemoteViews notificationView;
    private int notificationId;
    private Notification notification;
    private NotificationManager mNotificationManager;
    private boolean isPlaying;

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY);
        registerReceiver(broadcastReceiver, filter);

        generateNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(notificationId, notification);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void generateNotification() {
        notificationId = 5;

        notificationView = new RemoteViews(getPackageName(), R.layout.notification_controller);

        //the intent that is started when the notification is clicked (works)
        Intent notificationIntent = new Intent(this, ResultActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        //this is the intent that is supposed to be called when the button is clicked
        Intent switchIntent = new Intent(ACTION_PLAY);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0, switchIntent, 0);

        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingNotificationIntent)
                .setContentTitle("Custom View")
                .build(); //Mostrar layout pra view quando estiver pequena

        notification.bigContentView = notificationView;
        notification.flags += Notification.FLAG_NO_CLEAR;

        notificationView.setOnClickPendingIntent(R.id.pause_button, pendingSwitchIntent);

        isPlaying = true;

        updateNotification();
    }

    private void updateNotification() {
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, notification);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equalsIgnoreCase(ACTION_PLAY)) {
                if (isPlaying) {
                    notificationView.setImageViewResource(R.id.pause_button, R.drawable.play);
                    updateNotification();
                    isPlaying = false;
                } else {
                    notificationView.setImageViewResource(R.id.pause_button, R.drawable.pause);
                    updateNotification();
                    isPlaying = true;
                }
            }
        }
    };
}
