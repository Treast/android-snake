package fr.vincentriva.snake2.services.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class LinearAccelerationSensorListener implements SensorEventListener {
    private SensorService sensorService;

    LinearAccelerationSensorListener(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        this.sensorService.notifyValues(event.values[0], event.values[1], event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
