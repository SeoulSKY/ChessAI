package api;

import game.Game;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("api/initial-board")
@RestController
public class InitialBoardController {

    @GetMapping
    public String initialBoard() {
        return new Game().getInitialState().toString();
    }
}
