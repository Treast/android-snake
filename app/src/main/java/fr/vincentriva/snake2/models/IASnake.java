package fr.vincentriva.snake2.models;

import fr.vincentriva.snake2.utils.Vector2;
import fr.vincentriva.snake2.interfaces.ISnake;

public class IASnake extends ISnake {
    private Vector2<Integer> applePosition;

    public void lookAtApple(Vector2<Integer> applePosition) {
        this.applePosition = applePosition.clone();
    }

    @Override
    public void move() {
        int dX = position.getX() - applePosition.getX();
        int dY = position.getY() - applePosition.getY();

        if(dX == 0) {
            setVector(0, (dY > 0) ? -1 : 1);
        } else {
            setVector((dX > 0) ? -1 : 1, 0);
        }

        position.add(speed.getX(), speed.getY());
    }
}
