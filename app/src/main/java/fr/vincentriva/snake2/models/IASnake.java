package fr.vincentriva.snake2.models;

import fr.vincentriva.snake2.utils.Vector2;
import fr.vincentriva.snake2.interfaces.ISnake;

/**
 * Modèle représentant le serpent joué par l'IA
 */
public class IASnake extends ISnake {

    /**
     * Position de la pomme, soit l'objectif que l'IA souhaite atteindre
     */
    private Vector2<Integer> applePosition;

    /**
     * Définit l'objectif de l'IA
     * @param applePosition Position de la pomme dans l'espace de jeu
     */
    public void lookAtApple(Vector2<Integer> applePosition) {
        this.applePosition = applePosition.clone();
    }

    /**
     * Déplace le serpent
     * On calcule d'abord le différentiel entre la position actuelle du serpent et celui de la pomme
     * puis on définit le mouvement du serpent.
     * @return True si le serpent a bougé, false s'il est rentré dans un mur
     */
    @Override
    public boolean move() {
        int dX = position.getX() - applePosition.getX();
        int dY = position.getY() - applePosition.getY();

        if(dX == 0) {
            setVector(0, -1 * (int)Math.signum(dY));
        } else {
            setVector(-1 * (int)Math.signum(dX), 0);
        }

        Vector2<Integer> newPosition = position.clone();
        newPosition.add(speed.getX(), speed.getY());

        if (position.getX() >= 1 && position.getX() < board.getX() - 1 && position.getY() >= 1 && position.getY() < board.getY() - 1) {
            position = newPosition;
            stack.remove(0);

            // Si le serpent a mangé une pomme à la frame précédente, on la rajoute à son corps
            if(tempTail != null) {
                stack.add(tempTail);
                tempTail = null;
            }

            stack.add(position.clone());
            return true;
        }
        return false;
    }
}
