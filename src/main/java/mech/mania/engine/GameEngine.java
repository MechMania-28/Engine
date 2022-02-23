package mech.mania.engine;

import mech.mania.engine.action.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameEngine {
  private final GameState state;
  private final List<Boolean> executed;
  private GamePhaseType phase;

  public GameEngine() {
    this.state = new GameState();
    this.phase = GamePhaseType.USE;
    this.executed = Arrays.asList(new Boolean[4]);
    Collections.fill(executed, Boolean.FALSE);
  }

  /**
   * Executes a given action as an action of the current phase state.
   *
   * @param action Action to be executed.
   */
  public void execute(Action action) {
    if (executed.get(action.getExecutingPlayerIndex())) return;
    switch (phase) {
      case USE:
        state.executeUse((UseAction) action);
        break;
      case MOVE:
        state.executeMove((MoveAction) action);
        break;
      case ATTACK:
        state.executeAttack((AttackAction) action);
        break;
      case BUY:
        state.executeBuy((BuyAction) action);
        break;
    }
    executed.set(action.getExecutingPlayerIndex(), true);

    // If all players have executed an Action for the current phase, reset and move to the next
    // phase.
    if (executed.stream().allMatch(Boolean::valueOf)) {
      Collections.fill(executed, Boolean.FALSE);
      this.phase = phase.next();
    }
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
