package game;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import piece.Piece;
import util.Position;

public record Action(@JsonProperty Piece piece, @JsonIgnore Position newPosition) {

    /**
     * Return the x of the new position
     * @return the x
     */
    @JsonGetter
    public int x() {
        return this.newPosition.x();
    }

    /**
     * Return the y of the new position
     * @return the y
     */
    @JsonGetter
    public int y() {
        return this.newPosition.y();
    }

    public String toString() {
        return "(" + this.piece.getIcon() + ", " + this.newPosition.y() + ", " + this.newPosition.x() + ")";
    }
}
