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

import java.util.ArrayList;

import static android.content.Context.SENSOR_SERVICE;

public class SnakeView extends GridView {

    private PlayerSnake snake = null;
    private IASnake iaSnake = null;
    private Apple apple = null;

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;

    private Vector2<Double> motionCalibration = new Vector2<>(0.0, 0.0);
    private Vector2<Double> motionSensorData = new Vector2<>(0.0, 0.0);

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
        registerGyroscopeListener();

        resetTileList(1000);

        loadTile(TILE_SNAKE, r.getDrawable(R.drawable.ic_snake));
        loadTile(TILE_WALL, r.getDrawable(R.drawable.ic_wall));
        loadTile(TILE_EMPTY, r.getDrawable(R.drawable.ic_floor));
        loadTile(TILE_APPLE, r.getDrawable(R.drawable.ic_apple));
        loadTile(TILE_SNAKE_TAIL, r.getDrawable(R.drawable.ic_snake2));
    }

    public void resetCalibration() {
        motionSensorData.set(0.0, 0.0);
    }

    private void registerGyroscopeListener() {
        // Create a listener
        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values.length == 5) {
                    if (motionCalibration.getX() == 0 && motionCalibration.getY() == 0) {
                        motionCalibration.set((double) sensorEvent.values[0], (double) sensorEvent.values[1]);
                    }

                    motionSensorData.set((double) sensorEvent.values[0], (double) sensorEvent.values[1]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startGame() {
        snake = new PlayerSnake();
        iaSnake = new IASnake();
        apple = new Apple();
        currentMode = MODE_PLAY;
    }

    public void stopGame() {
        currentMode = MODE_STOP;
    }

    private RefreshHandler mRedrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            SnakeView.this.clearTiles();
            SnakeView.this.invalidate();
        }

        void sleep() {
            removeMessages(0);
            sendMessageDelayed(obtainMessage(0), (long) 400);
        }
    }

    ;

    private void showSnake() {
        if (snake != null) {
            snake.move();
            Vector2<Integer> snakePosition = snake.getPosition();
            setTile(TILE_SNAKE, snakePosition.getX(), snakePosition.getY());

            ArrayList<Vector2<Integer>> coordinates = snake.getTrail();
            for (int i = 0; i < coordinates.size() - 1; i += 1) {
                Vector2<Integer> coords = coordinates.get(i);
                setTile(TILE_SNAKE_TAIL, coords.getX(), coords.getY());
            }
        }
    }

    private void showApple() {
        if (apple != null) {
            Vector2<Integer> applePosition = apple.getPosition();
            setTile(TILE_APPLE, applePosition.getX(), applePosition.getY());
        }
    }

    private void checkCollision() {
        if (apple != null && snake != null) {
            snake.checkCollision(apple);
        }
    }

    /*
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

                    double diffX = Math.abs(xVirtual - this.snake.getPosition().getX());
                    double diffY = Math.abs(yVirtual - this.snake.getPosition().getY());

                    if(diffX > diffY) {
                        if(xVirtual > this.snake.getPosition().getX()) {
                            this.snake.setVector(1, 0);
                        } else {
                            this.snake.setVector(-1, 0);
                        }
                    } else {
                        if(yVirtual > this.snake.getPosition().getY()) {
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
    */

    @Override
    protected void updateTiles() {
        for (int x = 0; x < GridView.getNbTileX(); x++) {
            setTile(TILE_WALL, x, 0);
            setTile(TILE_WALL, x, mNbTileY - 1);
        }

        for (int y = 0; y < GridView.getNbTileY(); y++) {
            setTile(TILE_WALL, 0, y);
            setTile(TILE_WALL, mNbTileX - 1, y);
        }

        setSnakeMovement();

        showApple();
        showSnake();
        showIASnake();
        checkCollision();
    }

    private void showIASnake() {
        iaSnake.lookAtApple(apple.getPosition());
        iaSnake.move();
        Vector2<Integer> iaSnakePosition = iaSnake.getPosition();
        setTile(TILE_SNAKE, iaSnakePosition.getX(), iaSnakePosition.getY());

        iaSnake.checkCollision(apple);
    }

    public void setSnakeMovement() {
        Vector2<Double> motionData = motionSensorData.clone();
        motionData.substract(motionCalibration.getX(), motionCalibration.getY());

        if (snake != null) {
            if (Math.abs(motionData.getX()) > Math.abs(motionData.getY())) {
                if (motionData.getX() > 0) {
                    snake.setVector(0, 1);
                } else {
                    snake.setVector(0, -1);
                }
            } else {
                if (motionData.getY() > 0) {
                    snake.setVector(1, 0);
                } else {
                    snake.setVector(-1, 0);
                }
            }
        }
    }

    @Override
    public void clearTiles() {
        if (currentMode == MODE_PLAY) {
            for (int x = 0; x < mNbTileX; x++) {
                for (int y = 0; y < mNbTileY; y++) {
                    setTile(TILE_EMPTY, x, y);
                }
            }

            updateTiles();
        }

        mRedrawHandler.sleep();
    }
}
