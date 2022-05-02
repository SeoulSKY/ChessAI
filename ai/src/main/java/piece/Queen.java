package piece;

import game.Player;
import util.Direction;
import util.Position;

import java.util.Collection;

/**
 * The queen piece of the chess
 */
public class Queen extends Piece {

    public static final char BLACK_ICON = '♛';

    public static final char WHITE_ICON = '♕';

    protected static final Direction[] directions = {Direction.UPLEFT, Direction.UPRIGHT, Direction.DOWNLEFT,
            Direction.DOWNRIGHT, Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN};

    public Queen(Player player, Position position) {
        super(player, position);
    }

    @Override
    public Collection<Position> movements(Player opponent) {
        return this.movements(opponent, directions);
    }

    @Override
    public char getBlackIcon() {
        return BLACK_ICON;
    }

    @Override
    public char getWhiteIcon() {
        return WHITE_ICON;
    }
}
