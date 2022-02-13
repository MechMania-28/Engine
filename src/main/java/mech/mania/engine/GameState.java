package mech.mania.engine;

import mech.mania.engine.action.AttackAction;
import mech.mania.engine.action.BuyAction;
import mech.mania.engine.action.MoveAction;
import mech.mania.engine.action.UseAction;
import mech.mania.engine.player.PlayerState;

import java.util.Arrays;
import java.util.List;

public class GameState {

  /** Holds the {@link PlayerState} of each of the 4 players in player order. */
  private final List<PlayerState> playerStateList = Arrays.asList(new PlayerState[4]);

  /**
   * Executes a {@link mech.mania.engine.action.UseAction}.
   *
   * @param useAction The action to be executed.
   */
  public void executeUse(UseAction useAction) {}

  /**
   * Executes a {@link mech.mania.engine.action.MoveAction}.
   *
   * @param moveAction The action to be executed.
   */
  public void executeMove(MoveAction moveAction) {}

  /**
   * Executes a {@link mech.mania.engine.action.AttackAction}.
   *
   * @param attackAction The action to be executed.
   */
  public void executeAttack(AttackAction attackAction) {}

  /**
   * Executes a {@link mech.mania.engine.action.BuyAction}.
   *
   * @param buyAction The action to be executed.
   */
  public void executeBuy(BuyAction buyAction) {}

  /**
   * Compiles the most recent turn's executed actions for all 4 players.
   *
   * @return a GameTurn object representing the most recent turn.
   */
  public GameTurn renderTurn() {
    return new GameTurn();
  }
}
