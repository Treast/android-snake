package fr.vincentriva.snake2;

class Apple {

    private Vector2<Integer> position;
    private Vector2<Integer> board;

    Apple() {
        this.board = new Vector2<>(GridView.getNbTileX(), GridView.getNbTileY());
        this.position = new Vector2<>(0, 0);
        this.randomize();
    }

    void randomize() {
        int x = (int)Math.floor(Math.random() * this.board.getX().doubleValue());
        int y = (int)Math.floor(Math.random() * this.board.getY().doubleValue());
        this.position.set(x, y);
    }

    Vector2<Integer> getPosition() {
        return this.position;
    }
}
