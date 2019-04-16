package fr.vincentriva.snake2.models;

import fr.vincentriva.snake2.interfaces.ISnake;

/**
 * Modèle représentant le serpent joué par le joueur
 */
public class PlayerSnake extends ISnake {

    /**
     * Déplace le serpent basé sur le vecteur déplacement généré par les inputs de l'utilisateur
     * @return True si le serpent a bougé, false s'il est rentré dans un mur
     */
    @Override
    public boolean move() {
        int newX = position.getX() + speed.getX();
        int newY = position.getY() + speed.getY();

        if (newX > 0 && newX < board.getX() - 1 && newY > 0 && newY < board.getY() - 1) {
            position.set(newX, newY);

            stack.remove(0);

            // Si le serpent a mangé une pomme à la frame précédente, on la rajoute à son corps
            if(tempTail != null) {
                stack.add(tempTail);
                tempTail = null;
            }

            stack.add(position.clone());

            return true;
        } else {
            return false;
        }
    }

}
