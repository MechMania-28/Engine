package mech.mania.engine;

import mech.mania.engine.action.ActionSet;
import mech.mania.engine.player.PlayerState;

import java.util.Arrays;
import java.util.List;

/** Represents decisions made in one turn. */
public class GameTurn {

  /** A list of states of the 4 players. */
  private final List<PlayerState> playerStates = Arrays.asList(new PlayerState[4]);

  /** A list of decisions made by each of the 4 players on this turn, in order of player index. */
  private final List<ActionSet> decisions = Arrays.asList(new ActionSet[4]);

  public List<ActionSet> getDecisions() {
    return decisions;
  }
}
