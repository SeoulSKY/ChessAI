package piece;

import game.Player;
import util.Cursor;
import util.Direction;
import util.Position;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The piece of the chess
 */
public abstract class Piece {

    protected Player player;

    protected Position position;

    protected boolean isAlive;

    public Piece(Player player, Position position) {
        this.player = player;
        this.isAlive = true;
        this.position = position;
    }

    /**
     * Move the piece to the given position
     * @param newPosition the new position to move to
     */
    public void moveTo(Position newPosition) {
        if (this.isAlive()) {
            throw new IllegalStateException("Cannot move a dead piece.");
        }

        this.position = newPosition;
    }

    /**
     * Get the position of the piece
     * @return the position
     */
    public Position getPosition() {
        if (!this.isAlive()) {
            throw new IllegalStateException("This piece is dead and doesn't have a position.");
        }
        return this.position;
    }

    /**
     * Get the x of the piece
     * @return the x
     */
    public int getX() {
        return this.getPosition().getX();
    }

    /**
     * Get the y of the piece
     * @return the y
     */
    public int getY() {
        return this.getPosition().getY();
    }

    /**
     * Check if the piece is alive
     * @return true if it is, false otherwise
     */
    public boolean isAlive() {
        return this.isAlive;
    }

    /**
     * Check if the piece is dead
     * @return true if it is, false otherwise
     */
    public boolean isDead() {
        return !this.isAlive();
    }

    /**
     * Set the piece dead
     */
    public void kill() {
        this.isAlive = false;
    }

    /**
     * Return all possible movements of the piece
     * @param opponent the opponent player of the piece
     * @return the movements
     */
    public abstract Collection<Position> movements(Player opponent);

    /**
     * Return all possible movements of the given piece to the given direction
     * @param piece the piece to get the movements
     * @param opponent the opponent player of the given piece
     * @param direction the direction to get the movements
     * @return the movements to the direction
     */
    protected Collection<Position> movements(Piece piece, Player opponent, Direction direction) {
        List<Position> movements = new LinkedList<>();
        Cursor cursor = new Cursor(piece.getX(), piece.getY(), direction);

        cursor.move();
        while (cursor.canMove() && !piece.player.isOccupied(cursor.getPosition()) && !opponent.isOccupied(cursor.getPosition())) {
            movements.add(cursor.getPosition());
            cursor.move();
        }

        if (opponent.isOccupied(cursor.getPosition())) {
            movements.add(cursor.getPosition());
        }

        return movements;
    }

    /**
     * Clone this piece
     * @param player the player of the piece
     * @return the clone
     */
    public Piece clone(Player player) {
        try {
            Piece clone = (Piece) super.clone();
            clone.player = player;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public abstract String toString();
}
