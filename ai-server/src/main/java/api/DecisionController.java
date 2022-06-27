package api;

import game.Bot;
import game.DecisionRecord;
import game.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@CrossOrigin
@RequestMapping("api/decision")
@RestController
public class DecisionController {

    private static final Logger logger = LogManager.getLogger();

    /**
     * Return the AI's decision from the given board
     * @param intelligenceLevel the intelligence level of the AI. Must be at least 0
     * @param board the board
     * @param timeLimit the time limit the bot can think in seconds. Must be at least 5
     * @return the decision with relevant information
     */
    @GetMapping
    public DecisionRecord decision(@RequestParam int intelligenceLevel, @RequestParam String board, @RequestParam Optional<Integer> timeLimit) {
        if (intelligenceLevel < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "intelligenceLevel cannot be less than 0. Given: " + intelligenceLevel);
        } else if (timeLimit.isPresent() && timeLimit.get() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "timeLimit cannot be less than 5. Given: " + timeLimit.get());
        }

        State state = State.parse(board, true);

        logger.info("Received intelligenceLevel: {}", intelligenceLevel);
        logger.info("Received timeLimit: {}", timeLimit);
        logger.info("Received state:\n{}", state);
        logger.info("Thinking...");

        Stack<DecisionRecord> decisionStack = new Stack<>();

        if (timeLimit.isEmpty()) {
            decisionStack.push(new Bot(intelligenceLevel).decide(state));
        } else {
            // decide with iterative intelligence level
            List<CompletableFuture<?>> futures = new LinkedList<>();
            for (int i = 0; i < intelligenceLevel+1; i++) {
                final int currentIntelligenceLevel = i;
                CompletableFuture<?> future =
                        CompletableFuture.supplyAsync(() -> new Bot(currentIntelligenceLevel).decide(state))
                                .thenApply(decisionStack::push)
                                .orTimeout(timeLimit.get(), TimeUnit.SECONDS)
                                .exceptionally(e -> {
                                    if (!(e instanceof TimeoutException)) {
                                        logger.error(e);
                                    }
                                    return null;
                                });
                futures.add(future);
            }
            futures.forEach(CompletableFuture::join);
        }

        DecisionRecord decisionRecord = decisionStack.pop();

        logger.info("Moved {} to {} with Minimax value: {} after {} seconds, expanding {} nodes.",
                decisionRecord.actionTaken().piece().toString(), decisionRecord.actionTaken(), decisionRecord.minimaxValue(),
                (decisionRecord.timeTaken().toMillis() / 1000.0), decisionRecord.numNodesExpanded());

        logger.info("Result:{}", decisionRecord.result());

        return decisionRecord;
    }
}
