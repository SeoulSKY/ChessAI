package piece;

import game.Player;
import util.Position;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {

    public static final char BLACK_ICON = '♟';

    public static final char WHITE_ICON = '♙';

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
    public char getBlackIcon() {
        return BLACK_ICON;
    }

    @Override
    public char getWhiteIcon() {
        return WHITE_ICON;
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

        Position moveOneForward = new Position(x, y + 1);
        if (!this.player.isOccupied(moveOneForward) && !opponent.isOccupied(moveOneForward)) {
            movements.add(moveOneForward);
        }

        final int INITIAL_ROW = 1;
        Position moveTwoForward = new Position(x, y + 2);
        if (!this.player.isOccupied(moveTwoForward) && !opponent.isOccupied(moveTwoForward) && y == INITIAL_ROW) {
            movements.add(moveTwoForward);
        }

        List<Position> attacks = List.of(
                new Position(x - 1, y + 1),
                new Position(x + 1, y + 1));

        for (Position position : attacks) {
            if (Position.isWithinBound(position) && opponent.isOccupied(position)) {
                movements.add(position);
            }
        }

        return movements;
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

        Position moveOneForward = new Position(x, y - 1);
        if (!this.player.isOccupied(moveOneForward) && !opponent.isOccupied(moveOneForward)) {
            movements.add(moveOneForward);
        }

        final int INITIAL_ROW = 6;
        Position moveTwoForward = new Position(x, y - 2);
        if (!this.player.isOccupied(moveTwoForward) && !opponent.isOccupied(moveTwoForward) && y == INITIAL_ROW) {
            movements.add(moveTwoForward);
        }

        List<Position> attacks = List.of(
                new Position(x - 1, y - 1),
                new Position(x + 1, y - 1));

        for (Position position : attacks) {
            if (Position.isWithinBound(position) && opponent.isOccupied(position)) {
                movements.add(position);
            }
        }

        return movements;
    }
}
