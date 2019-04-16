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
    protected Vector2<Integer> tempTail;

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
        tempTail = new Vector2<>(x, y);
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

    public boolean checkCollisionWithItself() {
        boolean isCollided = false;
        for(int i = 0; i < stack.size() - 1; i += 1) {
            Vector2 tail = stack.get(i);
            if(tail.getX().equals(position.getX()) && tail.getY().equals(position.getY())) isCollided = true;
        }
        return isCollided;
    }

    public Vector2<Integer> getPosition() {
        return position;
    }
}
