package fr.vincentriva.snake2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity {

    private final String LOG_CAT = MainActivity.class.getName();
    private SnakeView snakeView;
    private Menu mMenu;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_CAT, getString(R.string.debug_creation_msg));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.snakeView = findViewById(R.id.snake_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_play:
                Log.d(LOG_CAT, "Pressed");
                this.isRunning = !this.isRunning;
                item.setIcon(this.isRunning ? R.drawable.ic_pikachu : R.drawable.ic_unicorn);

                if(this.isRunning) {
                    this.snakeView.resetCalibration();
                    this.snakeView.startGame();
                } else {
                    this.snakeView.stopGame();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
