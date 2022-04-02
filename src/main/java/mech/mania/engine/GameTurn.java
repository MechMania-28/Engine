package mech.mania.engine;

import com.fasterxml.jackson.annotation.JsonProperty;
import mech.mania.engine.action.AttackAction;
import mech.mania.engine.action.BuyAction;
import mech.mania.engine.action.MoveAction;
import mech.mania.engine.action.UseAction;
import mech.mania.engine.player.PlayerState;

import java.util.Arrays;
import java.util.List;

public class GameTurn {
  @JsonProperty("turn")
  public int turn;

  @JsonProperty("use_actions")
  public List<UseAction> uses = Arrays.asList(new UseAction[4]);

  @JsonProperty("move_actions")
  public List<MoveAction> moves = Arrays.asList(new MoveAction[4]);

  @JsonProperty("attack_actions")
  public List<AttackAction> attacks = Arrays.asList(new AttackAction[4]);

  @JsonProperty("buy_actions")
  public List<BuyAction> buys = Arrays.asList(new BuyAction[4]);

  /** A list of states of the 4 players. */
  @JsonProperty("player_states")
  public List<PlayerState> playerStates = Arrays.asList(new PlayerState[4]);
}
