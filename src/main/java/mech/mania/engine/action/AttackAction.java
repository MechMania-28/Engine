package mech.mania.engine.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mech.mania.engine.player.Item;

public class AttackAction extends Action {

    @JsonProperty("isValid")
    private boolean isValid = true;

    public void invalidate() {
        isValid = false;
    }
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

    @JsonCreator
    public AttackAction(@JsonProperty("executor")int executingPlayerIndex, @JsonProperty("target")int targetPlayerIndex) {
        super(executingPlayerIndex);
        this.targetPlayerIndex = targetPlayerIndex;
    }

}
