package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import piece.*;
import util.Position;

import java.util.*;
import java.util.stream.Stream;

/**
 * The state of the chess game
 */
public class State implements Cloneable {

    private static final Logger logger = LogManager.getLogger();

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

    /**
     * Find the piece at the given position
     * @param position the position
     * @return the piece
     */
    public Optional<Piece> findPiece(Position position) {
        Optional<Piece> piece = this.botPlayer.findPiece(position);

        if (piece.isPresent()) {
            return piece;
        }

        return this.humanPlayer.findPiece(position);
    }

    @Override
    public State clone() {
        try {
            State clone = (State) super.clone();
            this.botPlayer = this.botPlayer.clone();
            this.humanPlayer = this.humanPlayer.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw logger.throwing(new RuntimeException(e));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        for (char[] row : board) {
            Arrays.fill(row, '□');
        }

        Stream.of(this.botPlayer.allPieces(), this.humanPlayer.allPieces())
                .flatMap(Collection::stream)
                .forEach(piece -> board[piece.getY()][piece.getX()] = piece.getIcon());

        for (int i = 0; i < BOARD_SIZE; i++) {
            builder.append(board[i]).append('\n');
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
                Position position = new Position(j, i);
                switch (chars[j]) {
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
