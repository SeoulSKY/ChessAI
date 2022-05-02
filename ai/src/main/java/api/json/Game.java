package api.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Game(@JsonProperty("intelligence") int intelligence,
                   @JsonProperty("board") String board,
                   @JsonProperty("action") Action action) {}
