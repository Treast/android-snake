package fr.vincentriva.snake2;

public class Vector2<T extends Number> {

    private T x;
    private T y;

    Vector2(T x, T y) {
        this.set(x, y);
    }

    void set(T x, T y) {
        this.x = x;
        this.y = y;
    }

    void add(int dX, int dY) {
        Number newX = this.x.intValue() + dX;
        Number newY = this.y.intValue() + dY;
        this.set((T) newX, (T) newY);
    }

    void substract(double dX, double dY) {
        Number newX = this.x.doubleValue() + dX;
        Number newY = this.y.doubleValue() + dY;
        this.set((T) newX, (T) newY);
    }

    public Vector2<T> clone() {
        return new Vector2<>(this.getX(), this.getY());
    }

    T getX() {
        return (T) x;
    }

    public void setX(T x) {
        this.x = x;
    }

    T getY() {
        return (T) y;
    }

    public void setY(T y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if(obj instanceof Vector2) {
            Vector2 vector = (Vector2) obj;
            result = (this.x.equals(vector.x) && this.y.equals(vector.y));
        }
        return result;
    }
}
