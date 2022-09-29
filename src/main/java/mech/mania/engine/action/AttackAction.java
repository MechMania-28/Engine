package mech.mania.engine.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import mech.mania.engine.player.Item;
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttackAction extends Action {

    @JsonProperty("target")
    private int targetPlayerIndex;

    public static AttackAction DEFAULT(int playerIndex) {
        return new AttackAction(playerIndex, playerIndex);
    }

    public int getTargetPlayerIndex() {
        return targetPlayerIndex;
    }

    @JsonProperty("damage")
    private int damage;

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @JsonProperty("nullified")
    private boolean nullified = false;

    public void nullify() {
        nullified = true;
    }

    @JsonCreator
    public AttackAction(@JsonProperty("executor")int executingPlayerIndex, @JsonProperty("target")int targetPlayerIndex) {
        super(executingPlayerIndex);
        this.targetPlayerIndex = targetPlayerIndex;
    }

}
