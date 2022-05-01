package game;

import piece.Piece;
import util.Position;

public class Action {
    protected Piece piece;
    protected Position newPosition;

    public Action(Piece piece, Position newPosition) {
        this.piece = piece;
        this.newPosition = newPosition;
    }

    /**
     * Get the piece of the action
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Get the new position of the action
     * @return the new position
     */
    public Position getNewPosition() {
        return newPosition;
    }

    public String toString() {
        return "(" + this.piece.toString() + ", " + this.newPosition.getY() + ", " + this.newPosition.getX() + ")";
    }
}
