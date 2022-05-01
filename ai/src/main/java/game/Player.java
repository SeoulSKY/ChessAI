package game;

import piece.*;
import util.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Player implements Cloneable {

    protected static final int NUM_PAWNS = 8;

    protected static final int NUM_BISHOPS = 2;

    protected static final int NUM_KNIGHTS = 2;

    protected static final int NUM_ROOKS = 2;

    protected boolean isBot;

    protected List<Piece> pawns;

    protected List<Piece> bishops;

    protected List<Piece> knights;

    protected List<Piece> rooks;

    protected Piece queen;

    protected Piece king;


    public Player(boolean isBot) {
        this.isBot = isBot;
        this.pawns = new ArrayList<>(NUM_PAWNS);
        this.bishops = new ArrayList<>(NUM_BISHOPS);
        this.knights = new ArrayList<>(NUM_KNIGHTS);
        this.rooks = new ArrayList<>(NUM_ROOKS);

        int pawnRow = isBot ? 1 : 6;

        for (int j = 0; j < NUM_PAWNS; j++) {
            this.pawns.add(new Pawn(this, new Position(pawnRow, j)));
        }

        int pieceRow = isBot ? 0 : 7;

        final int BISHOP1_COL = 2;
        final int BISHOP2_COL = 5;
        this.bishops.add(new Bishop(this, new Position(pieceRow, BISHOP1_COL)));
        this.bishops.add(new Bishop(this, new Position(pieceRow, BISHOP2_COL)));

        final int KNIGHT1_COl = 1;
        final int KNIGHT2_COL = 6;
        this.knights.add(new Knight(this, new Position(pieceRow, KNIGHT1_COl)));
        this.knights.add(new Knight(this, new Position(pieceRow, KNIGHT2_COL)));

        final int ROOK1_COL = 0;
        final int ROOK2_COL = 7;
        this.rooks.add(new Rook(this, new Position(pieceRow, ROOK1_COL)));
        this.rooks.add(new Rook(this, new Position(pieceRow, ROOK2_COL)));

        final int KING_COL = 3;
        this.king = new King(this, new Position(pieceRow, KING_COL));

        final int QUEEN_COL = 4;
        this.queen = new Queen(this, new Position(pieceRow, QUEEN_COL));
    }

    /**
     * Check if this player is bot
     * @return true if it is, false otherwise
     */
    public boolean isBot() {
        return this.isBot;
    }

    /**
     * Return all pieces of the player including dead pieces
     */
    public Collection<Piece> allPieces() {
        List<Piece> newList = new LinkedList<>();
        newList.addAll(this.pawns);
        newList.addAll(this.bishops);
        newList.addAll(this.knights);
        newList.addAll(this.rooks);
        newList.add(king);
        newList.add(queen);
        return newList;
    }

    /**
     * Return all alive pieces of the player
     */
    public Collection<Piece> alivePieces() {
        return this.allPieces().stream()
                .filter(Piece::isAlive)
                .toList();
    }

    /**
     * Return all dead pieces of the player
     */
    public Collection<Piece> deadPieces() {
        return this.allPieces().stream()
                .filter(Piece::isDead)
                .toList();
    }

    /**
     * Check if the given x and y are occupied by any piece of the player
     */
    public boolean isOccupied(int x, int y) {
        return isOccupied(new Position(x, y));
    }

    /**
     * Check if the given position is occupied by any piece of the player
     */
    public boolean isOccupied(Position position) {
        return this.alivePieces().stream()
                .anyMatch(piece -> piece.getPosition().equals(position));
    }

    /**
     * Return all possible actions of the player
     * @return the actions
     */
    public Collection<Action> actions(Player opponent) {
        List<Action> actions = new LinkedList<>();

        for (Piece piece : this.alivePieces()) {
            for (Position movement : piece.movements(opponent)) {
                actions.add(new Action(piece, movement));
            }
        }

        return actions;
    }

    @Override
    public Player clone() {
        try {
            Player newPlayer = (Player) super.clone();
            this.pawns = this.pawns.stream().map(p -> p.clone(newPlayer)).toList();
            this.bishops = this.bishops.stream().map(p -> p.clone(newPlayer)).toList();
            this.knights = this.knights.stream().map(p -> p.clone(newPlayer)).toList();
            this.rooks = this.knights.stream().map(p -> p.clone(newPlayer)).toList();
            this.queen = this.queen.clone(newPlayer);
            this.king = this.king.clone(newPlayer);

            return newPlayer;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
