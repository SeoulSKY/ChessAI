package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.Action;
import game.Game;
import game.State;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import piece.Piece;
import util.Position;

@RequestMapping("api/result")
@RestController
public class ResultController {

    protected static final Game game = new Game();

    /**
     * Return the result of the action applied to the board
     * @param json the json containing the board and the action
     * @return the result board
     * @throws JsonProcessingException when the given request body is not a valid json
     */
    @PostMapping
    public String result(@RequestBody String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        JsonNode actionNode = jsonNode.get("action");
        JsonNode pieceNode = actionNode.get("piece");

        String board = mapper.convertValue(jsonNode.get("board"), String.class);
        State state = State.parse(board, false);

        int x = mapper.convertValue(pieceNode.get("x"), int.class);
        int y = mapper.convertValue(pieceNode.get("y"), int.class);
        Position oldPosition = new Position(x, y);

        x = mapper.convertValue(actionNode.get("x"), int.class);
        y = mapper.convertValue(actionNode.get("y"), int.class);

        Piece piece = state.getHumanPlayer().findPiece(oldPosition)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("A piece at %s not found on the given board.", oldPosition)));

        Action action = new Action(piece, new Position(x, y));

        return game.result(state, action).toString();
    }
}
