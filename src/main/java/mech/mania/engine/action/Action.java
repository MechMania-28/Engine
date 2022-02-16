package mech.mania.engine.action;

public abstract class Action {
  private int executingPlayerIndex;


  public Action(int executingPlayerIndex) {
    this.executingPlayerIndex = executingPlayerIndex;
  }
  public int getExecutingPlayerIndex() {
    return executingPlayerIndex;
  }

}
