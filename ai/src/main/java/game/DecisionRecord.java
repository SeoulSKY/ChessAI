package game;

import java.time.Duration;

/**
 * Record of the decision made by the bot
 */
public record DecisionRecord(
        Duration timeTaken,
        double minimaxValue,
        Action actionTaken,
        String resultBoard,
        int numNodesExpanded
) {}
