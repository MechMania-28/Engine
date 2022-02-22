package mech.mania.engine;

import mech.mania.engine.action.*;

import java.util.Arrays;
import java.util.List;

public class GameEngine {
  private final GameState state;
  private final List<Boolean> executed;
  private GamePhaseType phase;

  public GameEngine() {
    this.state = new GameState();
    this.phase = GamePhaseType.USE;
    this.executed = Arrays.asList(new Boolean[4]);
  }

  /**
   * Executes a given action as an action of the current phase state.
   * @param action Action to be executed.
   */
  public void execute(Action action) {

  }

  public void endPhase() {
    this.phase = phase.next();
  }

  public GameState getState() {
    return state;
  }

  public GamePhaseType getPhase() {
    return phase;
  }
}
