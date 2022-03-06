package mech.mania.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import mech.mania.engine.action.*;
import mech.mania.engine.networking.CommState;
import mech.mania.engine.networking.Server;
import mech.mania.engine.player.CharacterClass;
import mech.mania.engine.player.PlayerState;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameEngine {
  private final GameState gameState;
  private final List<Boolean> executed;
  private final Server gameServer;
  private GamePhaseType phase;
  private CommState commState;

  public GameEngine(int gamePort) {
    this.gameState = new GameState(Arrays.asList(new PlayerState[4]));
    this.phase = GamePhaseType.USE;
    this.executed = Arrays.asList(new Boolean[4]);
    Collections.fill(executed, Boolean.FALSE);
    gameServer = new Server(gamePort, 4);
    this.commState = CommState.START;
  }

  public static void main(String[] args) throws IOException {
    List<Integer> ports = Arrays.stream(args).map(Integer::valueOf).collect(Collectors.toList());
    int enginePort = ports.get(0);

    GameEngine engine = new GameEngine(enginePort);
    while (!engine.gameServer.isOpen()) engine.gameServer.open();

    while (engine.commState != CommState.END) {
      switch (engine.commState) {
        case START:
          engine.gameServer.writeAll("wake");
          engine.commState = CommState.NUM_ASSIGN;
          break;
        case NUM_ASSIGN:
          // Wait for a single digit number.
          for (int i = 0; i < 4; i++) {
            engine.gameServer.write(String.valueOf(i),i);
          }
          engine.commState = CommState.CLASS_REPORT;
          break;
        case CLASS_REPORT:
          List<String> reads = engine.gameServer.readAll();
          for (String read : reads)
            System.out.println(new ObjectMapper().readValue(read, CharacterClass.class));
          engine.commState = CommState.IN_GAME;
          break;
        case IN_GAME:
          //          engine.play();
          engine.commState = CommState.END;
          break;
      }
    }

    engine.gameServer.writeAll("fin");
    boolean end = false;
    while (!end) {
      List<String> reads = engine.gameServer.readAll();
      end = true;
      for (String read : reads)
        if (!read.equals("fin")) {
          end = false;
          break;
        }
    }
  }

  /**
   * Executes a given action as an action of the current phase state.
   *
   * @param action Action to be executed.
   */
  public void execute(Action action) {
    if (executed.get(action.getExecutingPlayerIndex())) return;
    switch (phase) {
      case USE:
        gameState.executeUse((UseAction) action);
        break;
      case MOVE:
        gameState.executeMove((MoveAction) action);
        break;
      case ATTACK:
        gameState.executeAttack((AttackAction) action);
        break;
      case BUY:
        gameState.executeBuy((BuyAction) action);
        break;
    }
    executed.set(action.getExecutingPlayerIndex(), true);

    // If all players have executed an Action for the current phase, reset and move to the next
    // phase.
    if (executed.stream().allMatch(Boolean::valueOf)) {
      Collections.fill(executed, Boolean.FALSE);
      this.phase = phase.next();
    }
  }

  public void endPhase() {
    this.phase = phase.next();
  }

  public GameState getGameState() {
    return gameState;
  }

  public GamePhaseType getPhase() {
    return phase;
  }
}
