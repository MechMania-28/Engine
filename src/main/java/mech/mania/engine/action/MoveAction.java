package mech.mania.engine.action;

import mech.mania.engine.player.Position;

public class MoveAction extends Action{
    private int x_dest;
    private int y_dest;

    public MoveAction(int executingPlayerIndex, int x_dest, int y_dest) {
        super(executingPlayerIndex);
        this.x_dest = x_dest;
        this.y_dest = y_dest;
    }
    public MoveAction(int executingPlayerIndex, Position destination) {
        super(executingPlayerIndex);
        this.x_dest = destination.getX();
        this.y_dest = destination.getY();
    }

    public Position getPosition() {
        return new Position(this.getX_dest(), this.getY_dest());
    }
    public int getX_dest() {
        return x_dest;
    }

    public int getY_dest() {
        return y_dest;
    }

}
