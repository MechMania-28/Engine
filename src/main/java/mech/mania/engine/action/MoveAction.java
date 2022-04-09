package mech.mania.engine.action;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mech.mania.engine.player.Position;

public class MoveAction extends Action{
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
