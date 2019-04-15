package fr.vincentriva.snake2.models;

import fr.vincentriva.snake2.views.GridView;
import fr.vincentriva.snake2.utils.Vector2;

public class Apple {

    private Vector2<Integer> position;
    private Vector2<Integer> board;

    public Apple() {
        this.board = new Vector2<>(GridView.getNbTileX(), GridView.getNbTileY());
        this.position = new Vector2<>(0, 0);
        this.randomize();
    }

    public void randomize() {
        int x = (int)Math.floor(Math.random() * this.board.getX().doubleValue());
        int y = (int)Math.floor(Math.random() * this.board.getY().doubleValue());
        this.position.set(x, y);
    }

    public Vector2<Integer> getPosition() {
        return this.position;
    }
}
