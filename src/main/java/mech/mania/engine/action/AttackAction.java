package mech.mania.engine.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AttackAction extends Action {
    private int targetPlayerIndex;

    @JsonCreator
    public AttackAction(@JsonProperty("executor")int executingPlayerIndex, int targetPlayerIndex) {
        super(executingPlayerIndex);
        this.targetPlayerIndex = targetPlayerIndex;
    }

}
