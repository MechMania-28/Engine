package mech.mania.engine.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AttackAction extends Action {

    @JsonCreator
    public AttackAction(@JsonProperty("executor")int executingPlayerIndex) {
        super(executingPlayerIndex);
    }

}
