package game;

import piece.Piece;
import util.Position;

public record Action(Piece piece, Position newPosition) {

    public String toString() {
        return "(" + this.piece.toString() + ", " + this.newPosition.y() + ", " + this.newPosition.x() + ")";
    }
}
