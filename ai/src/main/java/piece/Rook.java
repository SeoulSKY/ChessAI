package piece;

import game.Player;
import util.Direction;
import util.Position;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The rook piece of the chess
 */
public class Rook extends Piece {
    public Rook(Player player, Position position) {
        super(player, position);
    }

    @Override
    public Collection<Position> movements(Player opponent) {
        List<Position> newList =  new LinkedList<>();
        newList.addAll(movements(this, opponent, Direction.LEFT));
        newList.addAll(movements(this, opponent, Direction.RIGHT));
        newList.addAll(movements(this, opponent, Direction.UP));
        newList.addAll(movements(this, opponent, Direction.DOWN));

        return newList;
    }

    @Override
    public String toString() {
        return this.isBot() ? "♜" : "♖";
    }
}
