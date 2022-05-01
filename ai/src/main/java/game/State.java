package game;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * The state of the chess game
 */
public class State implements Cloneable {

    protected static final int BOARD_SIZE = 8;

    protected Player botPlayer;

    protected Player humanPlayer;

    protected boolean isBotTurn;

    protected boolean isTerminal;

    protected Player winner;

    protected String stringed;

    public State() {
        this.botPlayer = new Player(true);
        this.humanPlayer = new Player(false);
        this.isBotTurn = true;
        this.isTerminal = false;
        this.winner = null;
        this.stringed = this.toString();
    }

    /**
     * Get the bot player
     * @return the player
     */
    public Player getBotPlayer() {
        return this.botPlayer;
    }

    /**
     * Get the human player
     * @return the player
     */
    public Player getHumanPlayer() {
        return this.humanPlayer;
    }

    /**
     * Check if it is the bot turn
     * @return true if it is, false otherwise
     */
    public boolean isBotTurn() {
        return this.isBotTurn;
    }

    public boolean isHumanTurn() {
        return !this.isBotTurn();
    }

    /**
     * Set the outcome of the game
     * @param winner the winner of the game, or null for draw
     */
    public void setOutcome(@Nullable Player winner) {
        this.isTerminal = true;
        this.winner = winner;
    }

    /**
     * Check if the current state is a terminal state
     * @return true if it is, false otherwise
     */
    public boolean isTerminal() {
        return this.isTerminal;
    }

    /**
     * Return the winner of the game
     * @return the winner
     */
    public Optional<Player> getWinner() {
        if (!this.isTerminal()) {
            throw new IllegalStateException("The current state is not a terminal state.");
        }

        return this.winner == null ? Optional.empty() : Optional.of(this.winner);
    }

    @Override
    public State clone() {
        try {
            State clone = (State) super.clone();
            this.botPlayer = this.botPlayer.clone();
            this.humanPlayer = this.humanPlayer.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String[][] board = new String[BOARD_SIZE][BOARD_SIZE];

        Stream.concat(this.botPlayer.alivePieces().stream(), this.humanPlayer.alivePieces().stream())
                .toList()
                .forEach(piece -> board[piece.getY()][piece.getX()] = piece.toString());

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == null) {
                    board[i][j] = "□";
                }
            }
            builder.append(String.join(" ", board[i])).append('\n');
        }

        return builder.toString();
    }
}
