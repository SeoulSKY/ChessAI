package api.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Piece(@JsonProperty("icon") char icon,
                    @JsonProperty("x") int x,
                    @JsonProperty("y") int y) {}
