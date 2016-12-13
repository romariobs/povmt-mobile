package com.les.povmt.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.les.povmt.LoginActivity;
import com.les.povmt.R;

/**
 * Created by romariobs on 12/12/16.
 */

public class BroadcastReceiverTI extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 669;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("PITON", "chama");

        processStartNotification(context);
    }

    private void processStartNotification(Context context) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("PovMT")
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(R.color.colorAccent))
                .setContentText("Quanto tempo você investiu ontem?")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID,
                new Intent(context, LoginActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(context));

        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
