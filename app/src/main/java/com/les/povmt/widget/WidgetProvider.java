package com.les.povmt.widget;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.les.povmt.CreateActivity;
import com.les.povmt.PovMTActivity;
import com.les.povmt.R;

/**
 * Created by romariobs on 05/12/16.
 */

public class WidgetProvider extends AppWidgetProvider {

    private static final String ACTION_NOTIFICATION_WIDGET = "CAME_FROM_WIDGET";

    @Override
    public final void onUpdate(final Context context,
                               final AppWidgetManager appWidgetManager,
                               final int[] appWidgetIds) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NOTIFICATION_WIDGET);
        context.getApplicationContext().registerReceiver(broadcastReceiver, filter);

        final ComponentName myWidget = new ComponentName(context,
                WidgetProvider.class);

        final Intent intentExecute = new Intent(context,
                PovMTActivity.class); //// TODO: 05/12/16
        final PendingIntent pendingIntentExecute = PendingIntent
                .getActivity(context, 2, intentExecute, 0);

        final Intent intent = new Intent(context, CreateActivity.class); // TODO
        
        final PendingIntent appPendingIntent = PendingIntent
                .getActivity(context, 3, intent, 0);

        final Intent switchIntent = new Intent(ACTION_NOTIFICATION_WIDGET);
        final PendingIntent pendingNotificationIntent = PendingIntent.getBroadcast(context, 4, switchIntent, 0);

        final RemoteViews remoteViews =
                new RemoteViews(context.getPackageName(),
                        R.layout.widget_layout);

        remoteViews.setOnClickPendingIntent(R.id.app_widget_play,
                pendingIntentExecute);

        remoteViews.setOnClickPendingIntent(R.id.app_widget_stop,
                pendingNotificationIntent);

        // Tell the AppWidgetManager to perform an update on the current app
        // widget
        appWidgetManager.updateAppWidget(myWidget, remoteViews);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equalsIgnoreCase(ACTION_NOTIFICATION_WIDGET)) {
                int notificationId = 0;

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Eu vim do widget...")
                                .setContentText("... e não faço nada por enquanto!");

                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(notificationId, mBuilder.build());
            }
        }
    };
}
