package fr.vincentriva.snake2;

public class IASnake extends Snake {
    private Vector2<Integer> applePosition;

    void lookAtApple(Vector2<Integer> applePosition) {
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
