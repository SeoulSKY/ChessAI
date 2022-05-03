package api;

import game.Action;
import game.Game;
import game.State;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RequestMapping("api/actions")
@RestController
public class ActionsController {

    @GetMapping
    public Collection<Action> actions(@RequestParam String board) {
        Game game = new Game();
        State state = State.parse(board, false);
        return game.actions(state);
    }
}
