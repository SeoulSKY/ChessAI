package piece;

import game.Player;
import util.Position;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Player player, Position position) {
        super(player, position);
    }

    @Override
    public Collection<Position> movements(Player opponent) {
        if (this.isBot()) {
            return this.botMovements(opponent);
        }

        return this.humanMovements(opponent);
    }

    @Override
    public String toString() {
        return this.isBot() ? "♟" : "♙";
    }

    /**
     * Return all possible movements of the piece for bot player
     * @param opponent the opponent player of the piece
     * @return the movements
     */
    protected Collection<Position> botMovements(Player opponent) {
        int x = this.getX();
        int y = this.getY();

        List<Position> movements = new LinkedList<>();

        if (x == 1) {
            movements.add(new Position(x, y + 2));
        }

        List<Position> positions = List.of(
                new Position(x, y + 1),
                new Position(x - 1, y + 1),
                new Position(x + 1, y + 1));

        for (Position position : positions) {
            if (Position.isWithinBound(position) && opponent.isOccupied(position)) {
                movements.add(position);
            }
        }

        return movements.stream()
                .filter(p -> !this.player.isOccupied(p))
                .toList();
    }

    /**
     * Return all possible movements of the piece for human player
     * @param opponent the opponent player of the piece
     * @return the movements
     */
    protected Collection<Position> humanMovements(Player opponent) {
        int x = this.getX();
        int y = this.getY();

        List<Position> movements = new LinkedList<>();

        if (x == 6) {
            movements.add(new Position(x, y - 2));
        }

        List<Position> positions = List.of(
                new Position(x, y - 1),
                new Position(x - 1, y - 1),
                new Position(x + 1, y - 1));

        for (Position position : positions) {
            if (Position.isWithinBound(position) && opponent.isOccupied(position)) {
                movements.add(position);
            }
        }

        return movements.stream()
                .filter(p -> !this.player.isOccupied(p))
                .toList();
    }
}
