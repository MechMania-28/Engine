package mech.mania.engine.action;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import mech.mania.engine.player.Position;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveAction extends Action{

    public static MoveAction DEFAULT(int playerIndex) {
        return new MoveAction(playerIndex, new Position(-1, -1));
    }
    private final Position destination;
    @JsonCreator
    public MoveAction(@JsonProperty("executor") int executingPlayerIndex, @JsonProperty("destination") Position destination) {
        super(executingPlayerIndex);
        this.destination = destination;
    }

    public Position getDestination() {
        return destination;
    }




}
