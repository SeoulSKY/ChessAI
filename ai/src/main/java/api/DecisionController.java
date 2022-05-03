package api;

import game.Bot;
import game.DecisionRecord;
import game.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/decision")
@RestController
public class DecisionController {

    private static final Logger logger = LogManager.getLogger();

    @GetMapping
    public DecisionRecord decision(@RequestParam int intelligenceLevel, @RequestParam String board) {
        Bot bot = new Bot(intelligenceLevel);

        State state = State.parse(board, true);

        logger.info("Received state:\n{}", state);
        logger.info("Thinking...");
        DecisionRecord decisionRecord = bot.decide(state);

        logger.info("Moved {} to {} with Minimax value: {} after {} seconds, expanding {} nodes.",
                decisionRecord.actionTaken().piece().toString(), decisionRecord.actionTaken(), decisionRecord.minimaxValue(),
                (decisionRecord.timeTaken().toMillis() / 1000.0), decisionRecord.numNodesExpanded());

        logger.info("Result:\n{}", decisionRecord.resultBoard());

        return decisionRecord;
    }
}
