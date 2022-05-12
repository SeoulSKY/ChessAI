package game;

import org.jetbrains.annotations.Nullable;
import piece.Piece;

import java.util.Collection;

/**
 * The chess game
 */
public class Game {

    protected static final double NUM_KING_WEIGHT = 200.0;

    protected static final double NUM_QUEEN_WEIGHT = 9.0;

    protected static final double NUM_ROOKS_WEIGHT = 5.0;

    protected static final double NUM_BISHOPS_KNIGHTS_WEIGHT = 3.0;

    protected static final double NUM_PAWNS_WEIGHT = 1.0;

    protected static final double PAWNS_LOCATION_WEIGHT = 0.5;

    protected static final double MOBILITY_WEIGHT = 0.1;

    protected static final double MINIMAX_VALUE_RANGE = 245.5;

    /**
     * Get the initial state of the game
     * @return the state
     */
    public State getInitialState() {
        return new State();
    }

    /**
     * Check if the given state is bot turn
     * @param state the state
     * @return true if it is, false otherwise
     */
    public boolean isBotTurn(State state) {
        return state.isBotTurn();
    }

    /**
     * Check if the given state is human turn
     * @param state the state
     * @return true if it is, false otherwise
     */
    public boolean isHumanTurn(State state) {
        return !this.isBotTurn(state);
    }

    /**
     * Check if the given state is a terminal state
     * @param state the state
     * @return true if it is, false otherwise
     */
    public boolean isTerminal(State state) {
        return state.isTerminal();
    }

    /**
     * Return all possible actions of the given state
     * @param state the state
     * @return the actions
     */
    public Collection<Action> actions(State state) {
        return this.isBotTurn(state) ? state.getBotPlayer().actions(state.getHumanPlayer()) : state.getHumanPlayer().actions(state.getBotPlayer());
    }

    /**
     * Return the result of the action applied
     * @param state the current state
     * @param action the action
     * @return the new state
     */
    public State result(State state, @Nullable Action action) {
        State newState = state.clone();

        if (action == null) {
            newState.setOutcome(null);
            return newState;
        }

        Player myPlayer, opponent;
        if (action.piece().isBot()) {
            myPlayer = newState.getBotPlayer();
            opponent = newState.getHumanPlayer();
        } else {
            myPlayer = newState.getHumanPlayer();
            opponent = newState.getBotPlayer();
        }

        Piece myPiece = myPlayer.findPiece(action.piece().getPosition()).orElseThrow();
        myPiece.moveTo(action.newPosition());

        opponent.killPiece(myPiece.getPosition());

        boolean isMyKingAlive = myPlayer.countKings() == 1;
        boolean isOpponentKingAlive = opponent.countKings() == 1;

        if (myPlayer.allPieces().size() == 1 && isMyKingAlive && opponent.allPieces().size() == 1 && isOpponentKingAlive) {
            newState.setOutcome(null);
        } else if (!isMyKingAlive) {
            newState.setOutcome(opponent);
        } else if (!isOpponentKingAlive) {
            newState.setOutcome(myPlayer);
        }

        newState.moveToNextPlayerTurn();

        return newState;
    }

    /**
     * Calculate the utility of the given state
     * @param state a terminal state
     * @return the utility of the terminal state
     */
    public double utility(State state) {
        if (!this.isTerminal(state)) {
            throw new IllegalArgumentException("This method shouldn't be used for a non-terminal state.");
        }

        if (state.getWinner().isEmpty()) {
            return 0.0;
        }

        if (state.getWinner().get().isBot()) {
            return MINIMAX_VALUE_RANGE;
        } else {
            return -MINIMAX_VALUE_RANGE;
        }
    }

    /**
     * Calculate the evaluated value of the non-terminal state
     * @param state a non-terminal state
     * @return the evaluated value
     */
    public double evaluate(State state) {
        if (this.isTerminal(state)) {
            throw new IllegalArgumentException("This method shouldn't be used for a terminal state.");
        }

        Player botPlayer = state.getBotPlayer();
        Player humanPlayer = state.getHumanPlayer();

        return NUM_KING_WEIGHT * (botPlayer.countKings() - humanPlayer.countKings())
                + NUM_QUEEN_WEIGHT * (botPlayer.countQueens() - humanPlayer.countQueens())
                + NUM_ROOKS_WEIGHT * (botPlayer.countRooks() - humanPlayer.countRooks())
                + NUM_BISHOPS_KNIGHTS_WEIGHT * (
                        botPlayer.countBishops() - humanPlayer.countBishops()
                                + botPlayer.countKnights() - humanPlayer.countKnights())
                + NUM_PAWNS_WEIGHT * (botPlayer.countPawns() - humanPlayer.countPawns())
                - PAWNS_LOCATION_WEIGHT * (
                        botPlayer.countDoubledPawns() - humanPlayer.countDoubledPawns()
                                + botPlayer.countBlockedPawns(humanPlayer) - humanPlayer.countBlockedPawns(botPlayer)
                                + botPlayer.countIsolatedPawns() - humanPlayer.countIsolatedPawns())
                + MOBILITY_WEIGHT * (botPlayer.actions(humanPlayer).size() - humanPlayer.actions(botPlayer).size());
    }
}
