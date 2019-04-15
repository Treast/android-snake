package fr.vincentriva.snake2.services.web;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import org.w3c.dom.Document;

import java.net.URL;

import static android.os.Build.*;

public class WebService extends Service {

    private final int NOTIFICATION_CHANNEL_ID = 2020;
    private final String NOTIFICATION_CHANNEL_NAME = "WEBSERVICE_CHANNEL";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if(VERSION.SDK_INT >= VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Integer.toString(NOTIFICATION_CHANNEL_ID), NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            Notification.Builder notificationBuilder = new Notification.Builder(this, Integer.toString(NOTIFICATION_CHANNEL_ID));
            Notification notification = notificationBuilder.build();
            startForeground(NOTIFICATION_CHANNEL_ID, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LoginTask loginTask = new LoginTask(this);
        try {
            URL url = new URL("http://snake.struct-it.fr/login.php?user=snake&pwd=test");
            loginTask.execute(url);
        } catch(Exception e) {

        }
        return START_STICKY;
    }

    public void notifyLogin(Boolean aBoolean, Document document) {}
}
