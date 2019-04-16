package fr.vincentriva.snake2;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import fr.vincentriva.snake2.services.sensor.SensorReceiver;
import fr.vincentriva.snake2.services.sensor.SensorService;
import fr.vincentriva.snake2.views.SnakeView;

public class MainActivity extends AppCompatActivity {

    /**
     * View du jeu
     */
    private SnakeView snakeView;

    /**
     * TextView du résultat de la partie
     */
    private TextView resultGameTextView;

    /**
     * Booléen définissant si le jeu est en cours ou non
     */
    private boolean isRunning = false;

    /**
     * On récupère les ressources nécessaires
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        snakeView = findViewById(R.id.snake_view);
        resultGameTextView = findViewById(R.id.resultGameTextView);
        resultGameTextView.setVisibility(View.INVISIBLE);
    }

    /**
     * Au lancement de l'activitié, on initialise le service permettant de récupérer les données de l'accéléromètre
     * et un BroadcastReceiver pour dispatcher ses valeurs
     */
    @Override
    protected void onStart() {
        this.snakeView.setResultGameTextView(resultGameTextView);

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

    /**
     * Passe les données de l'accéléromètre à la View du jeu
     * @param linearAccelerationX Accélération sur l'axe X
     * @param linearAccelerationY Accélération sur l'axe Y
     * @param linearAccelerationZ Accélération sur l'axe Z
     */
    public void updateSensor(float linearAccelerationX, float linearAccelerationY, float linearAccelerationZ) {
        this.snakeView.setLinearAccelerationValues(linearAccelerationX, linearAccelerationY, linearAccelerationZ);
    }

    /**
     * Stoppe le service si l'application est arrêtée
     */
    @Override
    protected void onStop() {
        super.onStop();

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
                isRunning = !isRunning;
                item.setIcon(isRunning ? R.drawable.ic_pikachu : R.drawable.ic_unicorn);

                if(isRunning) {
                    snakeView.startGame();
                } else {
                    snakeView.stopGame();
                }

                return true;
            case R.id.menu_mode:
                snakeView.switchMovementMode(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
