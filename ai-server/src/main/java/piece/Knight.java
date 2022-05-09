package piece;

import game.Player;
import util.Position;

import java.util.Collection;
import java.util.List;

/**
 * The knight piece of the chess
 */
public class Knight extends Piece {

    public static final char BLACK_ICON = '♞';

    public static final char WHITE_ICON = '♘';

    public Knight(Player player, Position position) {
        super(player, position);
    }

    @Override
    public Collection<Position> movements(Player opponent) {
        int x = this.getX();
        int y = this.getY();
        
        List<Position> movements = List.of(
                new Position(x - 1, y - 2),
                new Position(x - 1, y + 2),
                new Position(x + 1, y - 2),
                new Position(x + 1, y + 2),
                new Position(x - 2, y - 1),
                new Position(x - 2, y + 1),
                new Position(x + 2, y - 1),
                new Position(x + 2, y + 1)
        );
        
        return movements.stream()
                .filter(Position::isWithinBound)
                .toList();
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
