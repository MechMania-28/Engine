package mech.mania.engine;

import mech.mania.engine.action.AttackAction;
import mech.mania.engine.action.BuyAction;
import mech.mania.engine.action.MoveAction;
import mech.mania.engine.action.UseAction;
import mech.mania.engine.player.PlayerState;

import mech.mania.engine.player.Position;
import mech.mania.engine.player.StatSet;
import mech.mania.engine.Utility;


import java.util.Arrays;
import java.util.List;

public class GameState {

  /** Holds the {@link PlayerState} of each of the 4 players in player order. */
  private final List<PlayerState> playerStateList = Arrays.asList(new PlayerState[4]);

  /** Constructor that takes a list of playerStates. */
  public GameState(List<PlayerState> players) {
    playerStateList.set(0, players.get(0));
    playerStateList.set(1, players.get(1));
    playerStateList.set(2, players.get(2));
    playerStateList.set(3, players.get(3));
  }

  public PlayerState getPlayerStateByIndex(int index) {
    return playerStateList.get(index);
  }
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

  public void executeMove(MoveAction moveAction) {

    // The intended destination of our move action
    int x_dest = moveAction.getX_dest();
    int y_dest = moveAction.getY_dest();

    // The player and stat set of said player that is attached to the action
    PlayerState currentPlayer = playerStateList.get(moveAction.getExecutingPlayerIndex());
    StatSet currentStatSet =  currentPlayer.getEffectiveStatSet();

    // Get the speed and current position of the player executing the action
    int speed = currentStatSet.getSpeed();
    int x_pos = currentPlayer.getPosition().getX();
    int y_pos = currentPlayer.getPosition().getY();

    // Check if the move is valid
    if ((Utility.inBounds(x_dest, y_dest)) && (speed >= Utility.manhattanDistance(x_pos, x_dest, y_pos, y_dest))) {

      // If it is then finally we can execute the move
      currentPlayer.getPosition().setX(x_dest);
      currentPlayer.getPosition().setY(y_dest);
    }

  }

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
