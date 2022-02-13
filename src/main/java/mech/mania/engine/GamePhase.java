package mech.mania.engine;

import mech.mania.engine.action.Action;

import java.util.Arrays;
import java.util.List;

public class GamePhase {
  /** A list of the actions taken by players in this phase, by player index. */
  private final List<Action> actions = Arrays.asList(new Action[4]);

}
