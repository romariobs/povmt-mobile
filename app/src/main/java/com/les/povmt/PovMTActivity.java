package com.les.povmt;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.les.povmt.notification.ItService;
import com.les.povmt.notification.ResultActivity;

public class PovMTActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pov_mt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWeekReport();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void startWeekReport() {
        Intent intent = new Intent(this, WeekReportActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pov_mt, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_activities) {
        } else if (id == R.id.nav_weekly_report) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    ///////////////// TODO: notification center ///////////////////
    ///////////////////////////////////////////////////////////////

    public void simpleNotification() {
        int notificationId = 0;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Simple notification")
                        .setContentText("TEXTO EXEMPLO");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    public void withActionNotification() {
        int notificationId = 1;

        Intent resultIntent = new Intent(this, ResultActivity.class);
        int requestCode = 0; //Private request code for the sender
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, requestCode, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Action notification")
                        .setContentText("Oi, eu tenho um resultado :)")
                        .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    public void bigViewNotification() {
        int notificationId = 2;
        String msg = "Texto grande para mostrar que a notificação fica grande e pode " +
                "conter muita coisa dentro, mas sem exageros porque o usuário não tem " +
                "paciência para ler tudo que você sempre sonhou em escrever em uma notificação.";

        Intent resultIntent = new Intent(this, ResultActivity.class);

        int requestCode = 0; //Private request code for the sender
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, requestCode, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText("Oi, eu sou grande!")
                        .setContentTitle("BigView notification")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    public void bigViewNotificationWithActions() {
        int notificationId = 3;
        String msg = "Agora eu tenho ações!\n\n" +
                "Texto grande para mostrar que a notificação fica grande e pode " +
                "conter muita coisa dentro, mas sem exageros porque o usuário não tem " +
                "paciência para ler tudo que você sempre sonhou em escrever em uma notificação.";

        Intent resultIntent = new Intent(this, ResultActivity.class);
        int requestCode = 90; //Private request code for the sender
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, requestCode, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent actionIntentOne = new Intent(this, ActionOneActivity.class); //TODO
        requestCode = 10;
        //PendingIntent actionPendingIntentOne =
        //        PendingIntent.getActivity(this, requestCode, actionIntentOne, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent actionIntentTwo = new Intent(this, ActionTwoActivity.class); //TODO
        requestCode = 20;
        //PendingIntent actionPendingIntentTwo =
         //       PendingIntent.getActivity(this, requestCode, actionIntentTwo, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(resultPendingIntent)
                        .setContentText("Oi, eu sou O GOKU!")
                        .setContentTitle("BigViewActions notification")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .addAction(R.drawable.ic_stat_dismiss, "Ação 1", actionPendingIntentOne) //TODO
                        .addAction(R.drawable.ic_stat_notification, "Ação 2", actionPendingIntentTwo); //// TODO: 06/12/16

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    public void progressBarNotification() {
        final int notificationId = 4;

        Intent resultIntent = new Intent(this, ResultActivity.class);
        int requestCode = 0; //Private request code for the sender
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, requestCode, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ProgressBar notification")
                        .setContentText("Oi, só estou mostrando. Não tem download.")
                        .setContentIntent(resultPendingIntent);

        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        for (incr = 0; incr <= 100; incr+=5) {
                            mBuilder.setProgress(100, incr, false);
                            // para barra de progresso iderteminada
                            //mBuilder.setProgress(0, 0, true);
                            mNotificationManager.notify(notificationId, mBuilder.build());
                            try {
                                Thread.sleep(1*1000);
                            } catch (InterruptedException e) {
                                Log.d("TAG", "sleep failure");
                            }
                        }
                        mBuilder.setContentText("Download complete")
                                .setProgress(0,0,false);
                        mNotificationManager.notify(notificationId, mBuilder.build());
                    }
                }
        ).start();
    }

    @SuppressLint("NewApi")
    public void setNotificationRemoteView() {
        startService(new Intent(this, ItService.class));
    }

    public void deleteNotification() {
        stopService(new Intent(this, ItService.class));

        final NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(
                        Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


}
