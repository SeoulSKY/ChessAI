package game;

import java.util.stream.Stream;

/**
 * The state of the chess game
 */
public class State implements Cloneable {

    protected static final int BOARD_SIZE = 8;

    protected Player botPlayer;

    protected Player humanPlayer;

    public State() {
        this.botPlayer = new Player(true);
        this.humanPlayer = new Player(false);
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
