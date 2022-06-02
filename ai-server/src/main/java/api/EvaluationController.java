package api;

import game.Game;
import game.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/evaluation")
@RestController
@CrossOrigin
public class EvaluationController {

    private static final Logger logger = LogManager.getLogger();

    protected static final Game game = new Game();

    @GetMapping
    public double evaluation(@RequestParam String board) {
        logger.info("Received Data: {}", board);
        return game.evaluate(State.parse(board, false));
    }
}
