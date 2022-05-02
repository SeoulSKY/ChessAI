package game;

import org.jetbrains.annotations.Nullable;
import piece.*;
import util.Position;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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

    public State() {
        this.botPlayer = new Player(true);
        this.humanPlayer = new Player(false);
        this.isBotTurn = true;
        this.isTerminal = false;
        this.winner = null;
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

    /**
     * Check if it is the human turn
     * @return true if it is, false otherwise
     */
    public boolean isHumanTurn() {
        return !this.isBotTurn();
    }

    /**
     * Move on to the next player's turn
     */
    public void moveToNextPlayerTurn() {
        this.isBotTurn = !this.isBotTurn();
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

        Stream.concat(this.botPlayer.allPieces().stream(), this.humanPlayer.allPieces().stream())
                .toList()
                .forEach(piece -> board[piece.getY()][piece.getX()] = piece.toString());

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == null) {
                    board[i][j] = "□";
                }
            }
            builder.append(String.join("", board[i])).append('\n');
        }

        return builder.toString();
    }

    /**
     * Parse the given board into the state
     * @param board the board
     * @param isBotTurn whether it is a bot turn or not
     * @return the state
     */
    public static State parse(String board, boolean isBotTurn) {
        Set<Piece> botPieces = new HashSet<>();
        Set<Piece> humanPieces = new HashSet<>();
        Player botPlayer = new Player(true, botPieces);
        Player humanPlayer = new Player(false, humanPieces);

        String[] lines = board.split("\n");

        for (int i = 0; i < lines.length; i++) {
            char[] chars = lines[i].toCharArray();
            for (int j = 0; j < lines[i].length(); j++) {
                Position position = new Position(i, j);
                switch (chars[i]) {
                    case Bishop.BLACK_ICON -> botPieces.add(new Bishop(botPlayer, position));
                    case Bishop.WHITE_ICON -> humanPieces.add(new Bishop(humanPlayer, position));
                    case King.BLACK_ICON -> botPieces.add(new King(botPlayer, position));
                    case King.WHITE_ICON -> humanPieces.add(new King(humanPlayer, position));
                    case Knight.BLACK_ICON -> botPieces.add(new Knight(botPlayer, position));
                    case Knight.WHITE_ICON -> humanPieces.add(new Knight(humanPlayer, position));
                    case Pawn.BLACK_ICON -> botPieces.add(new Pawn(botPlayer, position));
                    case Pawn.WHITE_ICON -> humanPieces.add(new Pawn(humanPlayer, position));
                    case Queen.BLACK_ICON -> botPieces.add(new Queen(botPlayer, position));
                    case Queen.WHITE_ICON -> humanPieces.add(new Queen(humanPlayer, position));
                    case Rook.BLACK_ICON -> botPieces.add(new Rook(botPlayer, position));
                    case Rook.WHITE_ICON -> humanPieces.add(new Rook(humanPlayer, position));
                }
            }
        }

        State state = new State();
        state.botPlayer = botPlayer;
        state.humanPlayer = humanPlayer;
        state.isBotTurn = isBotTurn;

        return state;
    }
}
