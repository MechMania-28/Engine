package mech.mania.engine;

import com.fasterxml.jackson.annotation.JsonProperty;
import mech.mania.engine.action.Action;
import mech.mania.engine.player.PlayerState;

import java.util.Arrays;
import java.util.List;

public class GamePhase {
  @JsonProperty("turn")
  public int turn;

  @JsonProperty("last_phase")
  public GamePhaseType prev_phase;

  @JsonProperty("next_phase")
  public GamePhaseType next_phase;

  /** A list of the actions taken by players in this phase, by player index. */
  @JsonProperty("last_actions")
  public List<Action> lastActions = Arrays.asList(new Action[4]);

  /** A list of states of the 4 players. */
  @JsonProperty("player_states")
  public List<PlayerState> playerStates = Arrays.asList(new PlayerState[4]);
}
