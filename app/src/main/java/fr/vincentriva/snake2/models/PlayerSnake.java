package fr.vincentriva.snake2.models;

import android.util.Log;

import fr.vincentriva.snake2.interfaces.ISnake;

public class PlayerSnake extends ISnake {

    @Override
    public boolean move() {
        int newX = position.getX() + speed.getX();
        int newY = position.getY() + speed.getY();

        if (newX > 0 && newX < board.getX() - 1 && newY > 0 && newY < board.getY() - 1) {
            position.set(newX, newY);

            stack.remove(0);

            if(tempTail != null) {
                stack.add(tempTail);
                tempTail = null;
            }

            stack.add(position.clone());

            return true;
        } else {
            return false;
        }
    }

}
