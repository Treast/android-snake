package fr.vincentriva.snake2.services.sensor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class SensorService extends Service {
    private final int NOTIFICATION_CHANNEL_ID = 2021;
    private final String NOTIFICATION_CHANNEL_NAME = "SENSORSERVICE_CHANNEL";
    public static final String INTENT_NAME = "fr.vincentriva.snake2.services.sensor.SENSOR_SERVICE";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SensorManager sensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        LinearAccelerationSensorListener linearAccelerationSensorListener = new LinearAccelerationSensorListener(this);
        sensorManager.registerListener(linearAccelerationSensorListener, sensor, Sensor.TYPE_LINEAR_ACCELERATION);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Integer.toString(NOTIFICATION_CHANNEL_ID), NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            Notification.Builder notificationBuilder = new Notification.Builder(this, Integer.toString(NOTIFICATION_CHANNEL_ID));
            Notification notification = notificationBuilder.build();
            startForeground(NOTIFICATION_CHANNEL_ID, notification);
        }
    }

    public void notifyValues(float linearAccelerationX, float linearAccelerationY, float linearAccelerationZ) {
        Intent intent = new Intent(INTENT_NAME);

        intent.putExtra("linearAccelerationX", linearAccelerationX);
        intent.putExtra("linearAccelerationY", linearAccelerationY);
        intent.putExtra("linearAccelerationZ", linearAccelerationZ);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
