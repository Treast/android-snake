package fr.vincentriva.snake2.views;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;

import fr.vincentriva.snake2.R;
import fr.vincentriva.snake2.models.Apple;
import fr.vincentriva.snake2.models.IASnake;
import fr.vincentriva.snake2.models.PlayerSnake;
import fr.vincentriva.snake2.utils.Vector2;

/**
 * Représentation de la grille du jeu
 */
public class SnakeView extends GridView {

    /**
     * Différents modes d'input de l'utilisateur
     * MODE_TOUCH: Le déplacement dépend de l'endroit où l'utilisateur appuye sur l'écran
     * MODE_ACCELERATION: Le déplacement dépend des valeurs fournies par l'accéléromètre de l'utilisateur
     */
    private final static String MODE_TOUCH = "MODE_TOUCH";
    private final static String MODE_ACCELERATION = "MODE_ACCELERATION";
    private String currentMovementMode = MODE_TOUCH;

    /**
     * Différents modèles du jeu
     */
    private PlayerSnake snake = null;
    private IASnake iaSnake = null;
    private Apple apple = null;

    /**
     * Valeurs de l'accéléromètre (MODE_ACCELERATION uniquement)
     */
    private float linearAccelerationX = 0.0f;
    private float linearAccelerationY = 0.0f;
    private float linearAccelerationZ = 0.0f;

    /**
     * TextView affiché lors de la fin d'une partie (WIN ou LOSE)
     */
    private TextView resultGameTextView;

    /**
     * Seuil au-dela duquel on accepte de changer la direction du serpent du joueur (MODE_ACCELERATION uniquement)
     * Si la valeur est inférieur, la direction n'est pas changée
     */
    private final float ACCELERATION_THRESHOLD = 5.0f;

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSnakeView(context);
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSnakeView(context);
    }

    /**
     * Définit les différents types de tiles utilisés dans le jeu
     * @param context Contexte de l'application
     */
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

    /**
     * Définit la TextView à utiliser pour afficher le résultat de la partie
     * @param resultGameTextView TextView du résultat
     */
    public void setResultGameTextView(TextView resultGameTextView) {
        this.resultGameTextView = resultGameTextView;
    }

    /**
     * Définit le texte à afficher dans la TextView du résultat et la rend visible
     * @param text Texte à afficher
     */
    private void setResultText(String text) {
        resultGameTextView.setText(text);
        resultGameTextView.setVisibility(VISIBLE);
    }

    /**
     * Définit les valeurs reçues par l'accéléromètre et les stocke
     * @param linearAccelerationX Accélération sur l'axe X
     * @param linearAccelerationY Accélération sur l'axe Y
     * @param linearAccelerationZ Accélération sur l'axe Z
     */
    public void setLinearAccelerationValues(float linearAccelerationX, float linearAccelerationY, float linearAccelerationZ) {
        this.linearAccelerationX = linearAccelerationX;
        this.linearAccelerationY = linearAccelerationY;
        this.linearAccelerationZ = linearAccelerationZ;
    }

    /**
     * Initialise les différents modèles, cache la TextView du résultat et lance la partie
     */
    public void startGame() {
        snake = new PlayerSnake();
        iaSnake = new IASnake();
        apple = new Apple();
        currentMode = MODE_PLAY;
        if(resultGameTextView != null) resultGameTextView.setVisibility(INVISIBLE);
    }

    /**
     * Arrête la partie (venant de l'utilisateur)
     */
    public void stopGame() {
        if(resultGameTextView != null) resultGameTextView.setVisibility(INVISIBLE);
        currentMode = MODE_STOP;
    }

    private RefreshHandler mRedrawHandler = new RefreshHandler();

    /**
     * Handler pour faire avancer le jeu toutes les 400 millisecondes
     */
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

    /**
     * Changement de mode de mouvement
     * @param menuMovementModeTextView TextView du bouton dans le menu
     */
    public void switchMovementMode(MenuItem menuMovementModeTextView) {
        currentMovementMode = (currentMovementMode.equals(MODE_TOUCH)) ? MODE_ACCELERATION : MODE_TOUCH;

        menuMovementModeTextView.setTitle((currentMovementMode.equals(MODE_TOUCH)) ? "Touch mode" : "Acceleration mode");
    }

    /**
     * Affiche le serpent du joueur (tête et corps)
     * Si le serpent ne peut plus bouger, sort de l'écran ou se mord lui-même, le jeu se finit
     */
    private void showSnake() {
        if(currentMode == MODE_PLAY) {
            if(!snake.move() || snake.isOutOfScreen() || snake.checkCollisionWithItself()) {
                setResultText("YOU LOSE !");
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


    /**
     * Affiche le serpent de l'IA (tête et corps) et définit son objectif (la pomme)
     * Si le serpent ne peut plus bouger, sort de l'écran ou se mord lui-même, le jeu se finit
     */
    private void showIASnake() {
        iaSnake.lookAtApple(apple.getPosition());

        if(currentMode == MODE_PLAY) {
            if(!iaSnake.move() || iaSnake.isOutOfScreen() || iaSnake.checkCollisionWithItself()) {
                setResultText("YOU WIN !");
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

    /**
     * Affiche la pomme
     */
    private void showApple() {
        Vector2<Integer> applePosition = apple.getPosition();
        setTile(TILE_APPLE, applePosition.getX(), applePosition.getY());
    }

    /**
     * Vérifie si le serpent du joueur entre en collision avec la pomme
     */
    private void checkCollision() {
        snake.checkCollision(apple);
    }

    /**
     * Définit le futur déplacement du serpent du joueur selon l'endroit où il appuye sur l'écran
     * On calcule la différence entre le point appuyé sur l'écran et la position du serpent (en X et en Y)
     * On définit ensuite le déplacement vers l'axe où la différence est la plus grande
     * @param event Event
     * @return True
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //This prevents touchscreen events from flooding the main thread
        synchronized (event) {
            try {
                //Waits 16ms.
                event.wait(16);

                if(currentMode == MODE_PLAY && currentMovementMode.equals(MODE_TOUCH) && event.getAction() == MotionEvent.ACTION_UP) {
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


    /**
     * Met à jour le jeu (affiche les murs et modèles, lance les mouvements et vérifie les collisions)
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

        checkCollision();

        setSnakeMovement();

        showApple();
        showSnake();
        showIASnake();
    }

    /**
     * Définit le futur mouvement du serpent du joueur (MODE_ACCELERATION uniquement)
     */
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

    /**
     * Nettoye l'écran de jeu
     */
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
