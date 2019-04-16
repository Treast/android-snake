package fr.vincentriva.snake2.utils;

/**
 * Classe permettant de réprensenter un espace 2D, une coordonnée, un vecteur direction ou un vecteur de vitesse
 * @param <T>
 */
public class Vector2<T extends Number> {

    /**
     * Composante en X
     */
    private T x;

    /**
     * Composante en Y
     */
    private T y;

    /**
     * Crée un vecteur 2D
     * @param x Composante en X
     * @param y Composante en Y
     */
    public Vector2(T x, T y) {
        this.set(x, y);
    }

    /**
     * Définit les composantes du vecteur 2D
     * @param x Nouvelle composante en X
     * @param y Nouvelle composante en Y
     */
    public void set(T x, T y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Ajoute des valeurs aux composantes
     * @param dX Valeur à ajouter en X
     * @param dY Valeur à ajouter en Y
     */
    public void add(int dX, int dY) {
        Number newX = this.x.intValue() + dX;
        Number newY = this.y.intValue() + dY;
        this.set((T) newX, (T) newY);
    }

    /**
     * Soustrait des valeurs aux composantes
     * @param dX Valeur à soustraire en X
     * @param dY Valeur à soustraire en Y
     */
    public void substract(double dX, double dY) {
        Number newX = this.x.doubleValue() + dX;
        Number newY = this.y.doubleValue() + dY;
        this.set((T) newX, (T) newY);
    }

    /**
     * Clone un vecteur 2D et retourne une nouvelle instance
     * @return Clone du vecteur 2D
     */
    public Vector2<T> clone() {
        return new Vector2<>(this.getX(), this.getY());
    }

    /**
     * Retourne la composante en X
     * @return Composante en X
     */
    public T getX() {
        return (T) x;
    }

    /**
     * Définit la composante en X
     * @param x Nouvelle composante en X
     */
    public void setX(T x) {
        this.x = x;
    }

    /**
     * Retourne la composante en Y
     * @return Composante en Y
     */
    public T getY() {
        return (T) y;
    }

    /**
     * Définit la composante en Y
     * @param y Nouvelle composante en Y
     */
    public void setY(T y) {
        this.y = y;
    }

    /**
     * Permet de comparer deux vecteurs et de savoir s'ils sont égaux
     * @param obj Object à comparer
     * @return True s'ils sont égaux, false sinon
     */
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
