package fr.vincentriva.snake2.interfaces;

import java.util.ArrayList;

import fr.vincentriva.snake2.models.Apple;
import fr.vincentriva.snake2.views.GridView;
import fr.vincentriva.snake2.utils.Vector2;

public abstract class ISnake {

    protected Vector2<Integer> board;
    protected Vector2<Integer> position;
    protected Vector2<Integer> speed;
    protected ArrayList<Vector2<Integer>> stack = new ArrayList<>();

    protected ISnake() {
        board = new Vector2<>(GridView.getNbTileX(), GridView.getNbTileY());
        position = new Vector2<>(board.getX() / 2, board.getY() / 2);
        speed = new Vector2<>(0, 1);

        stack.add(position.clone());
    }

    public void setVector(int x, int y) {
        speed.set(x, y);
    }

    public ArrayList<Vector2<Integer>> getTrail() {
        return stack;
    }

    private void addTail(int x, int y) {
        stack.add(new Vector2<>(x, y));
    }

    public abstract boolean move();

    public void checkCollision(Apple apple) {
        if(position.equals(apple.getPosition())) {
            addTail(position.getX(), position.getY());
            apple.randomize();
        }
    }

    public Boolean isOutOfScreen() {
        return position.getX() <= 0 || position.getX() >= board.getX() - 1 || position.getY() <= 0 || position.getY() >= board.getY() - 1;
    }

    public Vector2<Integer> getPosition() {
        return position;
    }
}
