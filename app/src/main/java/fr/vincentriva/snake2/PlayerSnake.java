package fr.vincentriva.snake2;

import java.util.ArrayList;

public class PlayerSnake {
    private int xTiles;
    private int yTiles;

    private int x;
    private int y;

    private int vX;
    private int vY;

    private ArrayList<Coordinates> stack = new ArrayList<>();

    public PlayerSnake() {
        this.xTiles = GridView.getNbTileX();
        this.yTiles = GridView.getNbTileY();

        this.x = xTiles / 2;
        this.y = yTiles / 2;

        this.stack.add(new Coordinates(this.x, this.y));

        vX = 0;
        vY = 1;
    }

    public void setVector(int x, int y) {
        this.vX = x;
        this.vY = y;
    }

    public ArrayList<Coordinates> getCoordinates() {
        return this.stack;
    }

    public void addTail(int x, int y) {
        this.stack.add(new Coordinates(x, y));
    }

    public void move() {
        int newX = this.x + this.vX;
        int newY = this.y + this.vY;

        if(this.x < 0 || this.x >= this.xTiles - 1 || y < 0 || y >= yTiles - 1) {

        } else {
            this.x = newX;
            this.y = newY;
        }
        this.stack.remove(0);
        this.stack.add(new Coordinates(this.x, this.y));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
