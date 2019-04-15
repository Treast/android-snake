package fr.vincentriva.snake2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fr.vincentriva.snake2.services.sensor.SensorReceiver;
import fr.vincentriva.snake2.services.sensor.SensorService;
import fr.vincentriva.snake2.services.web.WebService;
import fr.vincentriva.snake2.views.SnakeView;

public class MainActivity extends AppCompatActivity {

    private final String LOG_CAT = MainActivity.class.getName();
    private SnakeView snakeView;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_CAT, getString(R.string.debug_creation_msg));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        snakeView = findViewById(R.id.snake_view);
    }

    @Override
    protected void onStart() {
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WAKE_LOCK, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 1010);
        Intent intent = new Intent(this, SensorService.class);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        SensorReceiver sensorReceiver = new SensorReceiver(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(sensorReceiver, new IntentFilter(SensorService.INTENT_NAME));

        super.onStart();
    }

    public void updateSensor(float linearAccelerationX, float linearAccelerationY, float linearAccelerationZ) {
        this.snakeView.setLinearAccelerationValues(linearAccelerationX, linearAccelerationY, linearAccelerationZ);
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1010:
                if(permissions.length == 3) {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("ISnake", "Permissions granted");
                        Intent intent = new Intent(this, WebService.class);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent);
                        } else {
                            startService(intent);
                        }

                        SensorReceiver sensorReceiver = new SensorReceiver(this);
                        LocalBroadcastManager.getInstance(this).registerReceiver(sensorReceiver, new IntentFilter(SensorService.INTENT_NAME));
                    } else {
                        if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            Log.d("ISnake", "Wake Lock permission not granted");
                        }
                        if(grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                            Log.d("ISnake", "Internet permission not granted");
                        }
                        if(grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                            Log.d("ISnake", "Access network permission not granted");
                        }
                    }
                }
            break;
            default: break;
        }
    }
    */

    @Override
    protected void onStop() {
        super.onStop();

        //Intent intent = new Intent(this, WebService.class);
        //stopService(intent);

        Intent sensorIntent = new Intent(this, SensorService.class);
        stopService(sensorIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_play:
                Log.d(LOG_CAT, "Pressed");
                isRunning = !isRunning;
                item.setIcon(isRunning ? R.drawable.ic_pikachu : R.drawable.ic_unicorn);

                if(isRunning) {
                    //snakeView.resetCalibration();
                    snakeView.startGame();
                } else {
                    snakeView.stopGame();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
