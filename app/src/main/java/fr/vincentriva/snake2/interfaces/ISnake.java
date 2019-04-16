package fr.vincentriva.snake2.interfaces;

import java.util.ArrayList;

import fr.vincentriva.snake2.models.Apple;
import fr.vincentriva.snake2.views.GridView;
import fr.vincentriva.snake2.utils.Vector2;

/**
 * Classe abstraite définissant les bases d'un serpent
 */
public abstract class ISnake {

    /**
     * Représentation de la taille du terrain de jeu
     */
    protected Vector2<Integer> board;

    /**
     * Vecteur 2D indiquant la position du serpent
     */
    protected Vector2<Integer> position;

    /**
     * Vecteur 2D représentation le futur déplacement du serpent
     */
    protected Vector2<Integer> speed;

    /**
     * Liste des éléments du corps du serpent. Le dernier élément de la liste répresente la tête, les autres la queue.
     */
    protected ArrayList<Vector2<Integer>> stack = new ArrayList<>();

    /**
     * Position temporaire d'un nouvel élément à ajouter à la queue du serpent
     */
    protected Vector2<Integer> tempTail;

    /**
     * Constructeur du serpent, en définissant la taille du terrain, la position initiale, la direction par défaut.
     */
    protected ISnake() {
        board = new Vector2<>(GridView.getNbTileX(), GridView.getNbTileY());
        position = new Vector2<>(board.getX() / 2, board.getY() / 2);
        speed = new Vector2<>(0, 1);

        stack.add(position.clone());
    }

    /**
     * Définit une nouvelle direction pour le serpent
     * @param x Direction en X
     * @param y Direction en Y
     */
    public void setVector(int x, int y) {
        speed.set(x, y);
    }

    /**
     * Retourne les éléments du corps du serpent
     * @return Element du corps du serpent
     */
    public ArrayList<Vector2<Integer>> getTrail() {
        return stack;
    }

    /**
     * Rajoute un élément au corps du serpent
     * @param x Position en X du nouvel élément
     * @param y Position en Y du nouvel élément
     */
    private void addTail(int x, int y) {
        tempTail = new Vector2<>(x, y);
    }

    /**
     * Déplace le serpent
     * @return True si le serpent a bougé, false s'il a touché un mur
     */
    public abstract boolean move();

    /**
     * Vérifie si la tête du serpent se trouve sur la pomme
     * @param apple Pomme
     */
    public void checkCollision(Apple apple) {
        if(position.equals(apple.getPosition())) {
            addTail(position.getX(), position.getY());
            apple.randomize();
        }
    }

    /**
     * Vérifie si le serpent touche un mur ou sort de l'écran
     * @return True s'il touche un mur ou sort de l'écran, false sinon
     */
    public Boolean isOutOfScreen() {
        return position.getX() <= 0 || position.getX() >= board.getX() - 1 || position.getY() <= 0 || position.getY() >= board.getY() - 1;
    }

    /**
     * Vérifie si le serpent rentre en collision avec un élément de son corps
     * @return True s'il entre en collision, false sinon
     */
    public boolean checkCollisionWithItself() {
        boolean isCollided = false;
        for(int i = 0; i < stack.size() - 1; i += 1) {
            Vector2 tail = stack.get(i);
            if(tail.getX().equals(position.getX()) && tail.getY().equals(position.getY())) isCollided = true;
        }
        return isCollided;
    }

    /**
     * Retourne la position du serpent
     * @return Position du serpent
     */
    public Vector2<Integer> getPosition() {
        return position;
    }
}
