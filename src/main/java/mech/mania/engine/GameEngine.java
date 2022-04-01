package mech.mania.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import mech.mania.engine.action.*;
import mech.mania.engine.networking.CommState;
import mech.mania.engine.networking.Server;
import mech.mania.engine.player.CharacterClass;
import mech.mania.engine.player.PlayerState;
import mech.mania.engine.player.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameEngine {
  private final List<Boolean> executed;
  private final Server gameServer;
  private final GameLog log;
  private GameState gameState;
  private GamePhaseType phaseType;
  private CommState commState;
  private int turnCount = 0;

  public GameEngine(int gamePort) {
    this.phaseType = GamePhaseType.USE;
    this.executed = Arrays.asList(new Boolean[4]);
    Collections.fill(executed, Boolean.FALSE);

    gameServer = new Server(gamePort, 4);
    this.commState = CommState.START;
    log = new GameLog();
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
          for (int i = 0; i < 4; i++) {
            engine.gameServer.write(String.valueOf(i), i);
          }
          engine.commState = CommState.CLASS_REPORT;
          break;
        case CLASS_REPORT:
          List<String> reads = engine.gameServer.readAll();
          List<CharacterClass> playerClasses = new ArrayList<>();
          for (String read : reads) {
            CharacterClass characterClass =
                new ObjectMapper().readValue(read, CharacterClass.class);
            System.out.println(characterClass);
            playerClasses.add(characterClass);
          }
          engine.gameState = new GameState(playerClasses);
          engine.commState = CommState.IN_GAME;
          break;
        case IN_GAME:
          engine.play();
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
    engine.gameServer.close();

    System.out.println(engine.log.render());
  }

  /**
   * Executes a given action as an action of the current phase state.
   *
   * @param string Action to be executed as JSON.
   */
  public void execute(String string) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Action action = mapper.readValue(string, Action.class);
    if (executed.get(action.getExecutingPlayerIndex())) return;
    switch (phaseType) {
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
      endPhase();
    }
  }

  public void endPhase() {
    this.phaseType = phaseType.next();
  }

  public GameState getGameState() {
    return gameState;
  }

  public GamePhaseType getPhaseType() {
    return phaseType;
  }

  /**
   * Compiles the most recent turn's executed actions for all 4 players.
   *
   * @return a GameTurn object representing the most recent turn.
   */
  public GamePhase renderPhase() {
    GamePhase phase = new GamePhase();
    phase.playerStates = gameState.getPlayerStateList();
    phase.prev_phase = phaseType;
    phase.next_phase = phaseType.next();
    return phase;
  }

  public void play() throws IOException {
    gameServer.writeAll(renderPhase());
    List<String> reads = gameServer.readAll();
    for (String read : reads) {
      System.out.println(read);
      execute(read);
    }
    if (phaseType == GamePhaseType.BUY) turnCount++;
    if (turnCount == Config.TURNS) commState = CommState.END;
  }
}
