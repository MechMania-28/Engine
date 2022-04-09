package mech.mania.engine;

import mech.mania.engine.action.AttackAction;
import mech.mania.engine.action.BuyAction;
import mech.mania.engine.action.MoveAction;
import mech.mania.engine.action.UseAction;
import mech.mania.engine.player.*;

import mech.mania.engine.util.Utility;


import java.util.Arrays;
import java.util.List;

import static mech.mania.engine.Config.BOARD_SIZE;

public class GameState {

  /** Holds the {@link PlayerState} of each of the 4 players in player order. */
  private final List<PlayerState> playerStateList = Arrays.asList(new PlayerState[4]);

  /** Holds the {@link Position} (spawn point) of each of the 4 players in player order CW. */
  private final List<Position> spawnPoints = Arrays.asList(
          new Position(0, 0),
          new Position(BOARD_SIZE-1, 0),
          new Position(BOARD_SIZE-1, BOARD_SIZE-1),
          new Position(0, BOARD_SIZE-1)
  );

  /**
   * Constructor that takes a list of character classes for each of the players respectively.
   *
   * @param playerClasses List of character classes.
   */
  public GameState(List<CharacterClass> playerClasses) {
    for (int i = 0; i < 4; i++) {
     playerStateList.set(i, new PlayerState(playerClasses.get(i), spawnPoints.get(i)));
    }
  }

  public PlayerState getPlayerStateByIndex(int index) {
    return playerStateList.get(index);
  }

  public void updateItems() {
    for (int i = 0; i < 4; i++) {
      PlayerState player = this.getPlayerStateByIndex(i);

      // Effect timer gets set in use action

      // Negative effect timer means the item is permanent
      if (player.getItem().isPermanent()) {
        player.setEffectTimer(-1);
      }

      // If players item is still in effect
      if (player.getEffectTimer() > 0) {
        player.decrementEffectTimer();
      }

      // If the players item has ran out
      if (player.getEffectTimer() == 0) {
        player.setItem(Item.NONE);
        player.setEffectTimer(-1);
      }
    }
  }


  public List<PlayerState> getPlayerStateList() {
    return playerStateList;
  }

  /**
   * Executes a {@link mech.mania.engine.action.UseAction}.
   *
   * @param useAction The action to be executed.
   */
  public void executeUse(UseAction useAction) {
    PlayerState currentPlayer = getPlayerStateByIndex(useAction.getExecutingPlayerIndex());
    currentPlayer.getItem().affect(currentPlayer);
  }

  /**
   * Executes a {@link mech.mania.engine.action.MoveAction}.
   *
   * @param moveAction The action to be executed.
   */

  public void executeMove(MoveAction moveAction) {

    // The intended destination of our move action
    Position destination = moveAction.getDestination();

    // The player and stat set of said player that is attached to the action
    PlayerState currentPlayer = getPlayerStateByIndex(moveAction.getExecutingPlayerIndex());
    StatSet currentStatSet =  currentPlayer.getEffectiveStatSet();

    // Get the speed and current position of the player executing the action
    int speed = currentStatSet.getSpeed();

    // Check if the move is valid
    if ((Utility.inBounds(destination)) && (speed >= Utility.manhattanDistance(destination, currentPlayer.getPosition()))) {
      // If it is then finally we can execute the move
      currentPlayer.setPosition(destination);
    }
  }

  /**
   * Executes a {@link mech.mania.engine.action.AttackAction}.
   *
   * @param attackAction The action to be executed.
   */
  public void executeAttack(AttackAction attackAction) {

    PlayerState actor = getPlayerStateByIndex(attackAction.getExecutingPlayerIndex());
    PlayerState target = getPlayerStateByIndex(attackAction.getTargetPlayerIndex());
    int range = actor.getEffectiveStatSet().getRange();
    int damage = actor.getEffectiveStatSet().getDamage();

    // Check if in range and if target isn't itself
    if (range >= Utility.squareDistance(actor.getPosition(), target.getPosition()) && (actor != target)) {
      // PROCRUSTEAN_IRON check
      if (target.getItem() == Item.PROCRUSTEAN_IRON) {
        target.incrementCurrHealth(-1 * CharacterClass.WIZARD.getStatSet().getDamage());
      }
      else {
        target.incrementCurrHealth(-1 * damage);
      }
    }
    // If not in range, then nothing happens
  }

  /**
   * Executes a {@link mech.mania.engine.action.BuyAction}.
   *
   * @param buyAction The action to be executed.
   */
  public void executeBuy(BuyAction buyAction) {
    int index = buyAction.getExecutingPlayerIndex();
    PlayerState currentPlayer = getPlayerStateByIndex(index);
    Item item = buyAction.getItem();

    // If the current player has enough gold and is in their own spawnpoint.
    if ((currentPlayer.getGold() >= item.getCost()) && currentPlayer.getPosition().equals(spawnPoints.get(index))) {
      // Set the item and decrement the players gold
      currentPlayer.setItem(item);
      currentPlayer.decrementGold(item.getCost());
    }
  }

  public void endTurn(){
    for (PlayerState playerState: playerStateList) {
        playerState.incrementGold(Config.GOLD_PER_TURN);
//        if (Utility.onControlTile(playerState)) playerState.incrementScore();
    }
  }

}
