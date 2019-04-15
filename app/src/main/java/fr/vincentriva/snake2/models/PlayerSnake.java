package fr.vincentriva.snake2.models;

import fr.vincentriva.snake2.interfaces.ISnake;

public class PlayerSnake extends ISnake {

    @Override
    public boolean move() {
        int newX = position.getX() + speed.getX();
        int newY = position.getY() + speed.getY();

        if (newX > 0 && newX < board.getX() - 1 && newY > 0 && newY < board.getY() - 1) {
            position.set(newX, newY);
        } else {
            return false;
        }

        stack.remove(0);
        stack.add(position.clone());
        return true;
    }

}
