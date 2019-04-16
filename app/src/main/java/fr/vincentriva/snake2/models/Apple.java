package fr.vincentriva.snake2.models;

import fr.vincentriva.snake2.views.GridView;
import fr.vincentriva.snake2.utils.Vector2;

/**
 * Modèle représentant la pomme
 */
public class Apple {

    /**
     * Position de la pomme
     */
    private Vector2<Integer> position;

    /**
     * Représentation du terrain de jeu
     */
    private Vector2<Integer> board;

    /**
     * Crée une nouvelle pomme
     */
    public Apple() {
        this.board = new Vector2<>(GridView.getNbTileX(), GridView.getNbTileY());
        this.position = new Vector2<>(0, 0);
        this.randomize();
    }

    /**
     * Change la position de la pomme pour une place aléatoire à l'intérieur du terrain de jeu
     */
    public void randomize() {
        int x = (int)Math.floor(Math.random() * (this.board.getX().doubleValue() - 2.0)) + 1;
        int y = (int)Math.floor(Math.random() * (this.board.getY().doubleValue() - 2.0)) + 1;
        this.position.set(x, y);
    }

    /**
     * Retourne la position de la pomme
     * @return Position de la pomme
     */
    public Vector2<Integer> getPosition() {
        return this.position;
    }
}
