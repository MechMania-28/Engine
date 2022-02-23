package mech.mania.engine.action;


import mech.mania.engine.player.Position;

public class MoveAction extends Action{
    private Position destination;

    public MoveAction(int executingPlayerIndex, Position destination) {
        super(executingPlayerIndex);
        this.destination = destination;
    }

    public Position getPosition() {
        return destination;
    }




}
