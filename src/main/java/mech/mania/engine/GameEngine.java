package mech.mania.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import mech.mania.engine.action.*;
import mech.mania.engine.networking.CommState;
import mech.mania.engine.networking.Server;
import mech.mania.engine.player.CharacterClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameEngine {
  private final Server gameServer;
  private final GameLog log;
  public List<UseAction> uses = Arrays.asList(new UseAction[4]);
  public List<MoveAction> moves = Arrays.asList(new MoveAction[4]);
  public List<AttackAction> attacks = Arrays.asList(new AttackAction[4]);
  public List<BuyAction> buys = Arrays.asList(new BuyAction[4]);
  private GameState gameState;
  private GamePhaseType phaseType;
  private CommState commState;
  private int turnCount = 0;
  private List<Action> lastActions= Arrays.asList(new Action[4]);

  public GameEngine(int gamePort) {
    this.phaseType = null;
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
          engine.start();
          engine.commState = CommState.IN_GAME;
          break;
        case IN_GAME:
          engine.play();
          break;
      }
    }

    engine.gameServer.writeAll("fin");
    List<Boolean> ended = Arrays.asList(false, false, false, false);
    while (!ended.stream().allMatch(val->val=true)) {
      List<String> reads = engine.gameServer.readAll();
      for (int i = 0; i < 4; i++) {
        if (reads.get(i).equals("fin")) {
          ended.set(i, true);
        }
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
    switch (phaseType) {
      case USE:
        gameState.executeUse((UseAction) action);
        uses.set(action.getExecutingPlayerIndex(), (UseAction) action);
        break;
      case MOVE:
        gameState.executeMove((MoveAction) action);
        moves.set(action.getExecutingPlayerIndex(), (MoveAction) action);
        break;
      case ATTACK:
        gameState.executeAttack((AttackAction) action);
        attacks.set(action.getExecutingPlayerIndex(), (AttackAction) action);
        break;
      case BUY:
        gameState.executeBuy((BuyAction) action);
        buys.set(action.getExecutingPlayerIndex(), (BuyAction) action);
        break;
    }
    lastActions.set(action.getExecutingPlayerIndex(), action);
  }

  private void start(){
    endTurn();
    phaseType = GamePhaseType.USE;
    GamePhase phase = new GamePhase();
    Collections.copy(phase.playerStates, gameState.getPlayerStateList());
    phase.lastActions = null;
    phase.turn = turnCount;
    phase.prev_phase = null;
    phase.next_phase = GamePhaseType.USE;
    gameServer.writeAll(phase);
  }

  private void endTurn() {
    gameState.endTurn();
    GameTurn turn = renderTurn();
    log.addTurn(turn);
    turnCount++;
//    uses = Arrays.asList(new UseAction[4]);
//    moves = Arrays.asList(new MoveAction[4]);
//    attacks = Arrays.asList(new AttackAction[4]);
//    buys = Arrays.asList(new BuyAction[4]);
  }

  private void endPhase() {
    if (phaseType == GamePhaseType.BUY) {
      endTurn();
    }
    if (turnCount > Config.TURNS) {
      commState = CommState.END;
      return;
    }
    GamePhase phase = renderPhase();
    gameServer.writeAll(phase);
    this.phaseType = phaseType.next();
  }

  private GamePhase renderPhase() {
    GamePhase phase = new GamePhase();
    Collections.copy(phase.playerStates, gameState.getPlayerStateList());
    phase.lastActions = lastActions;
    phase.turn = turnCount;
    phase.prev_phase = phaseType;
    phase.next_phase = phaseType.next();
    return phase;
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
  public GameTurn renderTurn() {
    GameTurn turn = new GameTurn();
    Collections.copy(turn.playerStates, gameState.getPlayerStateList());
    Collections.copy(turn.uses, uses);
    Collections.copy(turn.moves, moves);
    Collections.copy(turn.attacks, attacks);
    Collections.copy(turn.buys, buys);
    turn.turn = turnCount;
    return turn;
  }

  public void play() throws IOException {
    // If all players have executed an Action for the current phase, reset and move to the next
    // phase.
    if (commState == CommState.END) return;
    List<String> reads = gameServer.readAll();
    for (String read : reads) {
      System.out.println(read);
      execute(read);
    }
    endPhase();
  }
}
