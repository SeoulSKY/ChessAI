package game;

import java.util.Collection;

/**
 * The chess game
 */
public class Game {

    protected int depthLimit;

    public Game(int depthLimit) {
        this.depthLimit = depthLimit;
    }

    /**
     * Get the initial state of the game
     * @return the initial state
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
     * Check if the given depth exceeded the depth limit of the game
     * @param depth the depth to check
     * @return true if it should, false otherwise
     */
    public boolean shouldCutOff(int depth) {
        return depth > this.depthLimit;
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
    public State result(State state, Action action) {
        return null;
    }

    /**
     * Calculate the utility of the given state
     * @param state a terminal state
     * @return the utility of the terminal state
     */
    public int utility(State state) {
        return 0;
    }

    /**
     * Calculate the evaluated value of the non-terminal state
     * @param state a non-terminal state
     * @return the evaluated value
     */
    public int evaluate(State state) {
        return 0;
    }
}
