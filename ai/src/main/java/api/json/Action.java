package api.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Action(@JsonProperty("piece") Piece piece,
                     @JsonProperty("x") int x,
                     @JsonProperty("y") int y) {}
