package api;

import game.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import piece.Piece;
import util.Position;

@RequestMapping("api/decision")
@RestController
public class DecisionController {

    private static final Logger logger = LogManager.getLogger();

    @PostMapping
    public DecisionRecord decision(@RequestBody api.json.Game body) {
        Game game = new Game();
        Bot bot = new Bot(body.intelligence());

        State state = State.parse(body.board(), false);
        Position position = new Position(body.action().x(), body.action().y());
        Action action = new Action(state.getHumanPlayer().findPiece(position).orElseThrow(), position);

        logger.info("Thinking...");
        DecisionRecord decisionRecord = bot.decide(game.result(state, action));
        Piece piece = decisionRecord.actionTaken().piece();
        logger.info("Moved (" + piece + ", " + piece.getPosition() + ") to " + body.action() +
                " with Minimax value: " + decisionRecord + " after " + (decisionRecord.timeTaken().toMillis() * 100.0) +
                " seconds, expanding " + decisionRecord.numNodesExpanded() + " nodes.");

        return decisionRecord;
    }
}
