package mech.mania.engine;

import mech.mania.engine.player.Item;
import mech.mania.engine.player.PlayerState;
import mech.mania.engine.player.Position;

import java.util.Arrays;
import java.util.List;

public class GameState {

  /** Holds the {@link PlayerState} of each of the 4 players in player order. */
  private final List<PlayerState> playerStateList = Arrays.asList(new PlayerState[4]);

  /**
   * Executes a {@link mech.mania.engine.action.UseAction} for a given player.
   *
   * @param playerIndex Index of the player performing the action.
   */
  public void executeUse(int playerIndex) {}

  /**
   * Executes a {@link mech.mania.engine.action.MoveAction} for a given player.
   *
   * @param playerIndex Index of the player performing the action.
   * @param position The position being moved to.
   */
  public void executeMove(int playerIndex, Position position) {}

  /**
   * Executes a {@link mech.mania.engine.action.AttackAction} for a given player.
   *
   * @param playerIndex Index of the player performing the action.
   * @param targetPlayerIndex Index of the player being targeted by the attack.
   */
  public void executeAttack(int playerIndex, int targetPlayerIndex) {}

  /**
   * Executes a {@link mech.mania.engine.action.BuyAction} for a given player.
   *
   * @param playerIndex Index of the player performing the action.
   * @param item The item being bought.
   */
  public void executeBuy(int playerIndex, Item item) {}

  /**
   * Compiles the most recent turn's executed actions for all 4 players.
   *
   * @return a GameTurn object representing the most recent turn.
   */
  public GameTurn renderTurn() {
    return new GameTurn();
  }
}
