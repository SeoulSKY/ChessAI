package piece;

import game.Player;
import util.Position;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The king piece of the chess
 */
public class King extends Piece {
    public King(Player player, Position position) {
        super(player, position);
    }

    @Override
    public Collection<Position> movements(Player opponent) {
        int x = this.getX();
        int y = this.getY();

        List<Position> movements = new LinkedList<>();

        for (int i = x-1; i < x+2; i++) {
            for (int j = y-1; j < y+2; j++) {
                movements.add(new Position(i, j));
            }
        }

        return movements.stream()
                .filter(p -> !this.player.isOccupied(p))
                .filter(Position::isWithinBound)
                .toList();
    }

    @Override
    public String toString() {
        return this.isBot() ? "♚" : "♔";
    }
}
