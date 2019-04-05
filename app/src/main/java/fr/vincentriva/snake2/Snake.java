package fr.vincentriva.snake2;

import java.util.ArrayList;

public abstract class Snake {

    Vector2<Integer> board;
    Vector2<Integer> position;
    Vector2<Integer> speed;
    ArrayList<Vector2<Integer>> stack = new ArrayList<>();

    Snake() {
        board = new Vector2<>(GridView.getNbTileX(), GridView.getNbTileY());
        position = new Vector2<>(board.getX() / 2, board.getY() / 2);
        speed = new Vector2<>(0, 1);

        stack.add(position.clone());
    }

    void setVector(int x, int y) {
        speed.set(x, y);
    }

    ArrayList<Vector2<Integer>> getTrail() {
        return stack;
    }

    private void addTail(int x, int y) {
        stack.add(new Vector2<>(x, y));
    }

    public abstract void move();

    void checkCollision(Apple apple) {
        if(position.equals(apple.getPosition())) {
            addTail(position.getX(), position.getY());
            apple.randomize();
        }
    }

    Vector2<Integer> getPosition() {
        return position;
    }
}
