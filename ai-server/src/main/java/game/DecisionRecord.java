package game;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Duration;

/**
 * Record of the decision made by the bot
 */
public record DecisionRecord(
        Duration timeTaken,
        double minimaxValue,
        Action actionTaken,
        ObjectNode result,
        int numNodesExpanded
) {}
