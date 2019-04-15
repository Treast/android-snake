package fr.vincentriva.snake2.services.sensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.vincentriva.snake2.MainActivity;

public class SensorReceiver extends BroadcastReceiver {

    private MainActivity mainActivity;

    public SensorReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null) {
            switch(intent.getAction()) {
                case SensorService.INTENT_NAME:
                    float linearAccelerationX = intent.getFloatExtra("linearAccelerationX", 0.0f);
                    float linearAccelerationY = intent.getFloatExtra("linearAccelerationY", 0.0f);
                    float linearAccelerationZ = intent.getFloatExtra("linearAccelerationZ", 0.0f);
                    this.mainActivity.updateSensor(linearAccelerationX, linearAccelerationY, linearAccelerationZ);
                    break;

                default: break;
            }
        }
    }
}
