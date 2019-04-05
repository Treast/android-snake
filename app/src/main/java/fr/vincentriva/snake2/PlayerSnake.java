package fr.vincentriva.snake2;

public class PlayerSnake extends Snake {

    @Override
    public void move() {
        int newX = position.getX() + speed.getX();
        int newY = position.getY() + speed.getY();

        if (position.getX() >= 0 && position.getX() < board.getX() - 1 && position.getY() >= 0 && position.getY() < board.getY() - 1) {
            position.set(newX, newY);
        }

        stack.remove(0);
        stack.add(position.clone());
    }

}
