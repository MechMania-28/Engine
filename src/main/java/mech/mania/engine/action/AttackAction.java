package mech.mania.engine.action;

public class AttackAction extends Action {
    private int targetPlayerIndex;

    public int getTargetPlayerIndex() {
        return targetPlayerIndex;
    }

    public AttackAction(int executingPlayerIndex, int targetPlayerIndex) {
        super(executingPlayerIndex);
        this.targetPlayerIndex = targetPlayerIndex;
    }

}
