package fr.vincentriva.snake2.models;

import fr.vincentriva.snake2.utils.Vector2;
import fr.vincentriva.snake2.interfaces.ISnake;

public class IASnake extends ISnake {
    private Vector2<Integer> applePosition;

    public void lookAtApple(Vector2<Integer> applePosition) {
        this.applePosition = applePosition.clone();
    }

    @Override
    public boolean move() {
        int dX = position.getX() - applePosition.getX();
        int dY = position.getY() - applePosition.getY();

        if(dX == 0) {
            setVector(0, (dY > 0) ? -1 : 1);
        } else {
            setVector((dX > 0) ? -1 : 1, 0);
        }

        Vector2<Integer> newPosition = position.clone();
        newPosition.add(speed.getX(), speed.getY());

        if (position.getX() >= 1 && position.getX() < board.getX() - 2 && position.getY() >= 1 && position.getY() < board.getY() - 2) {
            position = newPosition;
            return true;
        }
        return false;
    }
}
