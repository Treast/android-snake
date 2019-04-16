package fr.vincentriva.snake2.views;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import fr.vincentriva.snake2.R;
import fr.vincentriva.snake2.models.Apple;
import fr.vincentriva.snake2.models.IASnake;
import fr.vincentriva.snake2.models.PlayerSnake;
import fr.vincentriva.snake2.utils.Vector2;

public class SnakeView extends GridView {

    private final static String MODE_TOUCH = "MODE_TOUCH";
    private final static String MODE_ACCELERATION = "MODE_ACCELERATION";
    private String currentMovementMode = MODE_TOUCH;

    private PlayerSnake snake = null;
    private IASnake iaSnake = null;
    private Apple apple = null;

    private float linearAccelerationX = 0.0f;
    private float linearAccelerationY = 0.0f;
    private float linearAccelerationZ = 0.0f;

    private TextView resultGameTextView;

    private final float ACCELERATION_THRESHOLD = 5.0f;

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

        resetTileList(1000);

        loadTile(TILE_SNAKE, r.getDrawable(R.drawable.ic_snake));
        loadTile(TILE_WALL, r.getDrawable(R.drawable.ic_wall));
        loadTile(TILE_EMPTY, r.getDrawable(R.drawable.ic_floor));
        loadTile(TILE_APPLE, r.getDrawable(R.drawable.ic_apple));
        loadTile(TILE_SNAKE_TAIL, r.getDrawable(R.drawable.ic_snake2));
        loadTile(TILE_AI_SNAKE, r.getDrawable(R.drawable.ic_robot));
    }

    public void setResultGameTextView(TextView resultGameTextView) {
        this.resultGameTextView = resultGameTextView;
    }

    private void setResultText(String text) {
        resultGameTextView.setText(text);
        resultGameTextView.setVisibility(VISIBLE);
    }

    public void setLinearAccelerationValues(float linearAccelerationX, float linearAccelerationY, float linearAccelerationZ) {
        this.linearAccelerationX = linearAccelerationX;
        this.linearAccelerationY = linearAccelerationY;
        this.linearAccelerationZ = linearAccelerationZ;
    }

    public void startGame() {
        snake = new PlayerSnake();
        iaSnake = new IASnake();
        apple = new Apple();
        currentMode = MODE_PLAY;
        if(resultGameTextView != null) resultGameTextView.setVisibility(INVISIBLE);
    }

    public void stopGame() {
        if(resultGameTextView != null) resultGameTextView.setVisibility(INVISIBLE);
        currentMode = MODE_STOP;
    }

    private RefreshHandler mRedrawHandler = new RefreshHandler();

    public void switchMovementMode(MenuItem menuMovementModeTextView) {
        currentMovementMode = (currentMovementMode.equals(MODE_TOUCH)) ? MODE_ACCELERATION : MODE_TOUCH;

        menuMovementModeTextView.setTitle((currentMovementMode.equals(MODE_TOUCH)) ? "Touch mode" : "Acceleration mode");
    }

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

    private void showSnake() {
        if(currentMode == MODE_PLAY) {
            if(!snake.move() || snake.isOutOfScreen() || snake.checkCollisionWithItself()) {
                setResultText("YOU LOSE MOTHAFUCKA !");
                currentMode = MODE_END;
            }
        }

        Vector2<Integer> snakePosition = snake.getPosition();
        setTile(TILE_SNAKE, snakePosition.getX(), snakePosition.getY());

        ArrayList<Vector2<Integer>> coordinates = snake.getTrail();
        for (int i = 0; i < coordinates.size() - 1; i += 1) {
            Vector2<Integer> coords = coordinates.get(i);
            setTile(TILE_SNAKE_TAIL, coords.getX(), coords.getY());
        }
    }

    private void showIASnake() {
        iaSnake.lookAtApple(apple.getPosition());

        if(currentMode == MODE_PLAY) {
            if(!iaSnake.move() || iaSnake.isOutOfScreen()) {
                setResultText("YOU WIN MOTHAFUCKA !");
                currentMode = MODE_END;
            }
        }

        Vector2<Integer> iaSnakePosition = iaSnake.getPosition();
        setTile(TILE_AI_SNAKE, iaSnakePosition.getX(), iaSnakePosition.getY());

        ArrayList<Vector2<Integer>> coordinates = iaSnake.getTrail();
        for (int i = 0; i < coordinates.size() - 1; i += 1) {
            Vector2<Integer> coords = coordinates.get(i);
            setTile(TILE_SNAKE_TAIL, coords.getX(), coords.getY());
        }

        iaSnake.checkCollision(apple);
    }

    private void showApple() {
        Vector2<Integer> applePosition = apple.getPosition();
        setTile(TILE_APPLE, applePosition.getX(), applePosition.getY());
    }

    private void checkCollision() {
        snake.checkCollision(apple);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //This prevents touchscreen events from flooding the main thread
        synchronized (event) {
            try {
                //Waits 16ms.
                event.wait(16);

                if(currentMovementMode.equals(MODE_TOUCH) && event.getAction() == MotionEvent.ACTION_UP) {
                    int xInitRaw = (int) Math.floor(event.getRawX());
                    int yInitRaw = (int) Math.floor(event.getRawY());

                    double xVirtual = GridView.getTileX(xInitRaw);
                    double yVirtual = GridView.getTileY(yInitRaw);

                    double diffX = Math.abs(xVirtual - this.snake.getPosition().getX());
                    double diffY = Math.abs(yVirtual - this.snake.getPosition().getY());

                    if(diffX > diffY) {
                        this.snake.setVector((int)Math.signum(xVirtual - snake.getPosition().getX()), 0);
                    } else {
                        this.snake.setVector(0, (int)Math.signum(yVirtual - snake.getPosition().getY()));
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
            setTile(TILE_WALL, x, 0);
            setTile(TILE_WALL, x, mNbTileY - 1);
        }

        for (int y = 0; y < GridView.getNbTileY(); y++) {
            setTile(TILE_WALL, 0, y);
            setTile(TILE_WALL, mNbTileX - 1, y);
        }

        checkCollision();

        setSnakeMovement();

        showApple();
        showSnake();
        showIASnake();
    }

    public void setSnakeMovement() {
        switch(currentMovementMode) {
            case MODE_ACCELERATION:
                if (Math.abs(linearAccelerationY) > Math.abs(linearAccelerationX) && Math.abs(linearAccelerationY) > ACCELERATION_THRESHOLD) {
                    snake.setVector(0, (int)Math.signum(linearAccelerationY));
                } else if(Math.abs(linearAccelerationX) > ACCELERATION_THRESHOLD) {
                    snake.setVector((int)Math.signum(linearAccelerationX), 0);
                }
                break;
            default: break;
        }
    }


    @Override
    public void clearTiles() {
        if (currentMode != MODE_STOP) {
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
