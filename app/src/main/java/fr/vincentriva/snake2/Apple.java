package fr.vincentriva.snake2;

public class Apple {
    private int x;
    private int y;

    private int maxX;
    private int maxY;

    public Apple() {
        this.maxX = GridView.getNbTileX();
        this.maxY = GridView.getNbTileY();
        this.randomize();
    }

    public void randomize() {
        this.x = (int)Math.floor(Math.random() * this.maxX);
        this.y = (int)Math.floor(Math.random() * this.maxY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
