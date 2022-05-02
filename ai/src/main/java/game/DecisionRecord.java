package game;

import java.time.Duration;

/**
 * Record of the decision made by the bot
 */
public class DecisionRecord {

    protected Duration timeTaken;

    protected double minimaxValue;

    protected Action actionTaken;

    protected State resultState;

    protected int numNodesExpanded;

    public DecisionRecord(Duration timeTaken, double minimaxValue, Action actionTaken, State resultState, int numNodesExpanded) {
        this.timeTaken = timeTaken;
        this.minimaxValue = minimaxValue;
        this.actionTaken = actionTaken;
        this.resultState = resultState;
        this.numNodesExpanded = numNodesExpanded;
    }

    /**
     * Get the time taken for the decision
     * @return the time
     */
    public Duration getTimeTaken() {
        return timeTaken;
    }

    /**
     * Get the minimax value of the decision
     * @return the value
     */
    public double getMinimaxValue() {
        return minimaxValue;
    }

    /**
     * Get the action taken by the bot
     * @return the action
     */
    public Action getActionTaken() {
        return actionTaken;
    }

    /**
     * Get the result state from the action
     * @return the state
     */
    public State getResultState() {
        return resultState;
    }

    /**
     * Get the number of nodes expanded for the decision
     * @return the number
     */
    public int getNumNodesExpanded() {
        return numNodesExpanded;
    }
}
