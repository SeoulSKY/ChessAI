package game;

import piece.Piece;
import util.Position;

public class Action {
    protected Piece piece;
    protected Position moveTo;

    public Action(Piece piece, Position moveTo) {
        this.piece = piece;
        this.moveTo = moveTo;
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
    public Position getMoveTo() {
        return moveTo;
    }

    public String toString() {
        return "(" + this.piece.toString() + ", " + this.moveTo.getY() + ", " + this.moveTo.getX() + ")";
    }
}
