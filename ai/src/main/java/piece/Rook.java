package piece;

import game.Player;
import util.Direction;
import util.Position;

import java.util.Collection;

/**
 * The rook piece of the chess
 */
public class Rook extends Piece {

    protected static final Direction[] directions = {Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN};

    public Rook(Player player, Position position) {
        super(player, position);
    }

    @Override
    public Collection<Position> movements(Player opponent) {
        return this.movements(opponent, directions);
    }

    @Override
    public String toString() {
        return this.isBot() ? "♜" : "♖";
    }
}
