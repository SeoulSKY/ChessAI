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

    public static final char BLACK_ICON = '♝';

    public static final char WHITE_ICON = '♗';

    public Bishop(Player player, Position position) {
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
