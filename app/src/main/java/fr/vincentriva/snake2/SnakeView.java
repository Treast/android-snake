package fr.vincentriva.snake2;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import static android.content.Context.SENSOR_SERVICE;

public class SnakeView extends GridView {

    private Snake snake = null;
    private Apple apple = null;

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeSensorListener;
    private float calibrationX = 0;
    private float calibrationZ = 0;
    private float gyroscopeX = 0;
    private float gyroscopeZ = 0;

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSnakeView(context);
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSnakeView(context);
    }

    private void initSnakeView(Context context) {
        setFocusable(true);
        Resources r = context.getResources();

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        this.registerGyroscopeListener();

        this.resetTileList(1000);

        this.loadTile(this.TILE_SNAKE, r.getDrawable(R.drawable.ic_snake));
        this.loadTile(this.TILE_WALL, r.getDrawable(R.drawable.ic_wall));
        this.loadTile(this.TILE_EMPTY, r.getDrawable(R.drawable.ic_floor));
        this.loadTile(this.TILE_APPLE, r.getDrawable(R.drawable.ic_apple));
        this.loadTile(this.TILE_SNAKE_TAIL, r.getDrawable(R.drawable.ic_snake2));
    }

    public void resetCalibration() {
        gyroscopeX = 0;
        gyroscopeZ = 0;
    }

    private void registerGyroscopeListener() {
        // Create a listener
        gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.values.length == 5) {
                    if(calibrationX == 0) {
                        calibrationX = sensorEvent.values[0];
                    }

                    if(calibrationZ == 0) {
                        calibrationZ = sensorEvent.values[1];
                    }

                    gyroscopeX = sensorEvent.values[0];
                    gyroscopeZ = sensorEvent.values[1];
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startGame() {
        this.snake = new Snake();
        this.apple = new Apple();
        this.currentMode = MODE_PLAY;
    }

    public void stopGame() {
        this.currentMode = MODE_STOP;
    }

    private RefreshHandler mRedrawHandler = new RefreshHandler();
    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            SnakeView.this.clearTiles();
            SnakeView.this.invalidate();
        }
        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };

    private void showSnake() {
        if(this.snake != null) {
            this.snake.move();
            this.setTile(this.TILE_SNAKE, this.snake.getX(), this.snake.getY());

            ArrayList<Coordinates> coordinates = this.snake.getCoordinates();
            for(int i = 0; i < coordinates.size() - 1; i += 1) {
                Coordinates coords = coordinates.get(i);
                this.setTile(this.TILE_SNAKE_TAIL, coords.x, coords.y);
            }
        }
    }

    private void showApple() {
        if(this.apple != null) {
            this.setTile(this.TILE_APPLE, this.apple.getX(), this.apple.getY());
        }
    }

    private void checkCollision() {
        if(this.apple != null && this.snake != null) {
            if(this.snake.getX() == this.apple.getX() && this.apple.getY() == this.snake.getY()) {
                this.snake.addTail(this.snake.getX(), this.snake.getY());
                this.apple.randomize();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //This prevents touchscreen events from flooding the main thread
        synchronized (event) {
            try {
                //Waits 16ms.
                event.wait(16);

                if(false && event.getAction() == MotionEvent.ACTION_UP) {
                    int xInitRaw = (int) Math.floor(event.getRawX());
                    int yInitRaw = (int) Math.floor(event.getRawY());

                    double xVirtual = GridView.getTileX(xInitRaw);
                    double yVirtual = GridView.getTileY(yInitRaw);

                    double diffX = Math.abs(xVirtual - this.snake.getX());
                    double diffY = Math.abs(yVirtual - this.snake.getY());

                    if(diffX > diffY) {
                        if(xVirtual > this.snake.getX()) {
                            this.snake.setVector(1, 0);
                        } else {
                            this.snake.setVector(-1, 0);
                        }
                    } else {
                        if(yVirtual > this.snake.getY()) {
                            this.snake.setVector(0, 1);
                        } else {
                            this.snake.setVector(0, -1);
                        }
                    }
                }
            }
            catch (InterruptedException e)
            {
                return true;
            }
        }
        return true;
    }

    @Override
    protected void updateTiles() {
        for (int x = 0; x < GridView.getNbTileX(); x++) {
            setTile(this.TILE_WALL, x, 0);
            setTile(this.TILE_WALL, x, mNbTileY - 1);
        }

        for (int y = 0; y < GridView.getNbTileY(); y++) {
            setTile(this.TILE_WALL, 0, y);
            setTile(this.TILE_WALL, mNbTileX - 1, y);
        }

        this.setSnakeMovement();

        this.showApple();
        this.showSnake();
        this.checkCollision();
    }

    public void setSnakeMovement() {
        float xAxis = gyroscopeX - calibrationX;
        float zAxis = gyroscopeZ - calibrationZ;
        Log.d("Gyroscope", xAxis + " ; " + zAxis);
        if(this.snake != null) {
            if(Math.abs(xAxis) >  Math.abs(zAxis)) {
                if(xAxis > 0) {
                    this.snake.setVector(0, 1);
                } else {
                    this.snake.setVector(0, -1);
                }
            } else {
                if(zAxis > 0) {
                    this.snake.setVector(1, 0);
                } else {
                    this.snake.setVector(-1, 0);
                }
            }
        }
    }

    @Override
    public void clearTiles() {
        if(this.currentMode == MODE_PLAY) {
            for (int x = 0; x < mNbTileX; x++) {
                for (int y = 0; y < mNbTileY; y++) {
                    setTile(TILE_EMPTY, x, y);
                }
            }

            this.updateTiles();
        }

        mRedrawHandler.sleep(400);
    }
}
