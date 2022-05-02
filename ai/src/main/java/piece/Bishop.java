package piece;

import game.Player;
import util.Direction;
import util.Position;

import java.util.Collection;

/**
 * The bishop piece of the chess
 */
public class Bishop extends Piece {

    protected static final Direction[] directions = {Direction.UPLEFT, Direction.UPRIGHT, Direction.DOWNLEFT,
            Direction.DOWNRIGHT};

    public Bishop(Player player, Position position) {
        super(player, position);
    }

    @Override
    public Collection<Position> movements(Player opponent) {
        return this.movements(opponent, directions);
    }

    @Override
    public String toString() {
        return this.isBot() ? "♝" : "♗";
    }
}
