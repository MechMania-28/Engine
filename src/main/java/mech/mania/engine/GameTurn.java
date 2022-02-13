package mech.mania.engine;

import mech.mania.engine.player.PlayerState;

import java.util.Arrays;
import java.util.List;

/** Represents decisions made in one turn. */
public class GameTurn {

  /** A list of states of the 4 players. */
  private final List<PlayerState> playerStates = Arrays.asList(new PlayerState[4]);

  /** A list of the 4 phases on this turn, in order of play. */
  private final List<GamePhase> phases = Arrays.asList(new GamePhase[4]);

  public List<GamePhase> getPhases() {
    return phases;
  }
}
