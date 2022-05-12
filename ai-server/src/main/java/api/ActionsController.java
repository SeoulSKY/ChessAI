package api;

import game.Action;
import game.Game;
import game.State;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin
@RequestMapping("api/actions")
@RestController
public class ActionsController {

    protected static final Game game = new Game();

    /**
     * Return the possible actions of the human player from the given board
     * @param board the board
     * @return the actions
     */
    @GetMapping
    public Collection<Action> actions(@RequestParam String board) {
        State state = State.parse(board, false);
        return game.actions(state);
    }
}
