package mech.mania.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import mech.mania.engine.action.AttackAction;
import mech.mania.engine.action.BuyAction;
import mech.mania.engine.action.MoveAction;
import mech.mania.engine.action.UseAction;
import mech.mania.engine.player.*;

import mech.mania.engine.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static mech.mania.engine.Config.MAX_PLAYERS;

public class GameState {

  /** Holds the {@link PlayerState} of each of the 4 players in player order. */
  private final List<PlayerState> playerStateList = Arrays.asList(new PlayerState[4]);

  /** Holds the {@link Position} (spawn point) of each of the 4 players in player order CW. */


  /**
   * Constructor that takes a list of character classes for each of the players respectively.
   *
   * @param playerClasses List of character classes.
   */
  public GameState(List<CharacterClass> playerClasses) {
    for (int i = 0; i < 4; i++) {
     playerStateList.set(i, new PlayerState(playerClasses.get(i), Utility.spawnPoints.get(i), i));
    }
  }

  public PlayerState getPlayerStateByIndex(int index) {
    return playerStateList.get(index);
  }

  public void updateItemsAtBeginTurn() {
    for (PlayerState player : playerStateList) {

      if (player.getEffectTimer() > 0) {
        player.decrementEffectTimer();
      }
      if (player.getEffectTimer() == 0) {
        player.setItemInEffect(Item.NONE);
        player.setEffectTimer(-1);
      }



      // Negative effect timer means the item is permanent
      if (player.getItemInEffect().isPermanent()) {
        player.setEffectTimer(-1);
      }

      // If players item is still in effect

    }
  }

  public void updateItems() {
    for (int i = 0; i < 4; i++) {
      PlayerState player = this.getPlayerStateByIndex(i);

      // Effect timer gets set in use action



      // If the players item has run out


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
    if (useAction == null) {
      return;
    }



    PlayerState currentPlayer = getPlayerStateByIndex(useAction.getExecutingPlayerIndex());

    if (currentPlayer.getItemHolding().isPermanent()) {

      useAction.invalidate();

      return;
    }

    if (useAction.shouldUse()) {
      currentPlayer.useItem();
      currentPlayer.getItemInEffect().affect(currentPlayer);
    }

  }

  private Position fixBadDestination(Position destination, PlayerState playerState) {
    int speed = playerState.getEffectiveStatSet().getSpeed();
    int minDistance = 100;
    Position fixedDestination = new Position(0, 0);
    for (int x = 0; x < Config.BOARD_SIZE; x++) {
      for (int y = 0; y < Config.BOARD_SIZE; y++) {
        Position currentPosition = new Position(x, y);
        int distance = Utility.manhattanDistance(currentPosition, destination);
        if (speed >= Utility.manhattanDistance(currentPosition, playerState.getPosition()) && distance < minDistance) {
          minDistance = distance;
          fixedDestination = new Position(x, y);
        }
      }
    }
    return fixedDestination;
  }

  /**
   * Executes a {@link mech.mania.engine.action.MoveAction}.
   *
   * @param moveAction The action to be executed.
   */

  public void executeMove(MoveAction moveAction) {

    if (moveAction == null) {
      return;
    }

    // The intended destination of our move action
    Position destination = moveAction.getDestination();

    // The player and stat set of said player that is attached to the action
    PlayerState currentPlayer = getPlayerStateByIndex(moveAction.getExecutingPlayerIndex());
    StatSet currentStatSet = currentPlayer.getEffectiveStatSet();

    // Get the speed and current position of the player executing the action
    int speed = currentStatSet.getSpeed();

    // Check if the move is valid
    if (destination.equals(Utility.spawnPoints.get(moveAction.getExecutingPlayerIndex())) ||
            ((Utility.inBounds(destination)) && (speed >= Utility.manhattanDistance(destination, currentPlayer.getPosition())))) {
      // If it is then finally we can execute the move
      currentPlayer.setPosition(destination);
    } else {
      Position goodDestination = fixBadDestination(destination, currentPlayer);
      currentPlayer.setPosition(goodDestination);
      moveAction.invalidate();
    }
  }

  private List<AttackAction> attackActionQueue = new ArrayList<>();
  private boolean isIndexOutOfBounds(AttackAction attackAction) {
    int executorIndex = attackAction.getExecutingPlayerIndex();
    int targetIndex = attackAction.getTargetPlayerIndex();
    return executorIndex < 0 || executorIndex >= MAX_PLAYERS || targetIndex < 0 || targetIndex >= MAX_PLAYERS;
  }
  private boolean isNotOutOfRangeOrSelfAttack(AttackAction attackAction) {
    PlayerState executor = getPlayerStateByIndex(attackAction.getExecutingPlayerIndex());
    PlayerState target = getPlayerStateByIndex(attackAction.getTargetPlayerIndex());
    int range = executor.getEffectiveStatSet().getRange();
    return range >= Utility.squareDistance(executor.getPosition(), target.getPosition()) && (executor != target);
  }

  public void queueAttack(AttackAction attackAction) {

    attackActionQueue.add(attackAction);
    if (attackActionQueue.size() == MAX_PLAYERS) {
      attackActionQueue.sort((aa1, aa2) ->
        getPlayerStateList().get(aa2.getExecutingPlayerIndex()).getEffectiveStatSet().getDamage()
                - getPlayerStateList().get(aa1.getExecutingPlayerIndex()).getEffectiveStatSet().getDamage()
      );
      executeAttackQueue();
      attackActionQueue.clear();
    }
  }

  public void executeAttackQueue() {
    boolean checkedShield = false;
    for (AttackAction attackAction : attackActionQueue) {
      if (!checkedShield &&
              !isIndexOutOfBounds(attackAction) &&
              isNotOutOfRangeOrSelfAttack(attackAction)) {
        executeAttack(attackAction, true);
        checkedShield = true;
      } else {
        executeAttack(attackAction, false);
      }

    }
  }

  public void executeAttack(AttackAction attackAction, boolean checkShield) {
    PlayerState executor = getPlayerStateByIndex(attackAction.getExecutingPlayerIndex());
    PlayerState target = getPlayerStateByIndex(attackAction.getTargetPlayerIndex());

    if (attackAction == null) {
      return;
    }

    if (isIndexOutOfBounds(attackAction)) {
      attackAction.invalidate();
      return;
    }


    if (checkShield && target.isShielded()) {
      attackAction.nullify();
      return;
    }

    int damage = executor.getEffectiveStatSet().getDamage();

    /*add damage figures to attackaction.*/
    attackAction.setDamage(damage);


    // Check if in range and if target isn't itself
    if (isNotOutOfRangeOrSelfAttack(attackAction)) {

      // PROCRUSTEAN_IRON check
      if (target.getItemHolding() == Item.PROCRUSTEAN_IRON) {
        target.incrementCurrHealth(-1 * CharacterClass.WIZARD.getStatSet().getDamage());
      }
      else {
        target.incrementCurrHealth(-1 * damage);
      }

      if (target.isDead()) {
        executor.incrementScore();
      }
    } else {
      attackAction.invalidate();
    }
    // If not in range, then invalidate action
  }

  /**
   * Executes a {@link mech.mania.engine.action.AttackAction}.
   *
   * @param attackAction The action to be executed.
   */
  public void executeAttack(AttackAction attackAction) {
    executeAttack(attackAction, false);
  }

  /**
   * Executes a {@link mech.mania.engine.action.BuyAction}.
   *
   * @param buyAction The action to be executed.
   */
  public void executeBuy(BuyAction buyAction) {
    if (buyAction == null) {
      return;
    }

    int index = buyAction.getExecutingPlayerIndex();
    PlayerState currentPlayer = getPlayerStateByIndex(index);
    Item item = buyAction.getItem();

    // If the current player has enough gold and is in their own spawn point.
    if ((currentPlayer.getGold() >= item.getCost()) && currentPlayer.getPosition().equals(Utility.spawnPoints.get(index))) {
      // Set the item and decrement the players gold
      currentPlayer.setItemHolding(item);
      currentPlayer.decrementGold(item.getCost());
    } else {
      if (item != Item.NONE)
        buyAction.invalidate();
    }
  }

  public void endTurn(){
    int index = 0;
    for (PlayerState playerState: playerStateList) {

      /* handle death item drop */
      if (playerState.getCurrHealth() == 0) {
        playerState.setItemHolding(Item.NONE);
      }

      playerState.incrementGold(Config.GOLD_PER_TURN);
      playerState.checkAndHandleBase(index);

      playerState.refreshHealth();

      if (Utility.onControlTile(playerState.getPosition())) {
          playerState.incrementScore();
          playerState.incrementScore();
        }
        playerState.setShielded(false);
      index++;
    }
    updateItems();

  }

  public void beginTurn() {
    updateItemsAtBeginTurn();
    int index = 0;
    for (PlayerState playerState : playerStateList) {
      playerState.checkAndHandleDeath(index);
      playerState.checkAndHandleBase(index);
      index++;
    }
  }

  public void removePlayer(int index) {
    playerStateList.get(index).setActive(false);
  }


}
