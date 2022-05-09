package piece;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import game.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger();

    protected Player player;

    protected Position position;

    public Piece(Player player, Position position) {
        this.player = player;
        this.position = position;
    }

    /**
     * Check if this is a bot piece
     * @return true if it is, false otherwise
     */
    @JsonIgnore
    public boolean isBot() {
        return this.player.isBot();
    }

    /**
     * Move the piece to the given position
     * @param newPosition the new position to move to
     */
    public void moveTo(Position newPosition) {
        this.position = newPosition;
    }

    /**
     * Get the position of the piece
     * @return the position
     */
    @JsonIgnore
    public Position getPosition() {
        return this.position;
    }

    /**
     * Get the icon of this piece
     * @return the icon
     */
    @JsonGetter
    public char getIcon() {
        return this.isBot() ? this.getBlackIcon() : this.getWhiteIcon();
    }

    /**
     * Get the x of the piece
     * @return the x
     */
    @JsonGetter
    public int getX() {
        return this.getPosition().x();
    }

    /**
     * Get the y of the piece
     * @return the y
     */
    @JsonGetter
    public int getY() {
        return this.getPosition().y();
    }

    /**
     * Return all possible movements of the piece
     * @param opponent the opponent player of the piece
     * @return the movements
     */
    public abstract Collection<Position> movements(Player opponent);

    /**
     * Get a string that represents this piece for the black player
     * @return the icon
     */
    @JsonIgnore
    protected abstract char getBlackIcon();

    /**
     * Get a string that represents this piece for the white player
     * @return the icon
     */
    @JsonIgnore
    protected abstract char getWhiteIcon();

    /**
     * Return all possible movements of the given piece to the given directions
     * @param opponent the opponent player of the given piece
     * @param directions the directions to get the movements
     * @return the movements to the direction
     */
    protected Collection<Position> movements(Player opponent, Direction[] directions) {
        List<Position> movements = new LinkedList<>();
        for (Direction direction : directions) {
            Cursor cursor = new Cursor(this.getX(), this.getY(), direction);

            while (cursor.canMove() && !opponent.isOccupied(cursor.getPosition())) {
                cursor.move();
                if (!this.player.isOccupied(cursor.getPosition())) {
                    movements.add(cursor.getPosition());
                }
            }
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
            return this.getClass()
                    .getDeclaredConstructor(Player.class, Position.class)
                    .newInstance(player, this.position);
        } catch (Exception e) {
            throw logger.throwing(new RuntimeException(e));
        }
    }

    public String toString() {
        return String.format("(%s, %s)", this.getIcon(), this.position);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        Piece other = (Piece) obj;
        return this.isBot() == other.isBot() && this.getPosition().equals(other.getPosition()) &&
                this.getIcon() == other.getIcon();
    }
}
