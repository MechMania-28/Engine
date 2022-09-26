package mech.mania.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import mech.mania.engine.action.*;
import mech.mania.engine.networking.CommState;
import mech.mania.engine.networking.Server;
import mech.mania.engine.player.CharacterClass;
import mech.mania.engine.player.PlayerState;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
    private List<Action> lastActions = Arrays.asList(new Action[4]);

    private static final String output = ".\\log.json";

    public static final Logger LOGGER = LogManager.getLogger(GameEngine.class.getName());
    static {
        Configurator.setLevel(LogManager.getLogger(GameEngine.class).getName(), Level.DEBUG);
    }

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

                    int index = 0;
                    for (String read : reads) {
                        GameEngine.LOGGER.debug(read);
                        CharacterClass characterClass =
                                new ObjectMapper().readValue(read, CharacterClass.class);
                        if (characterClass == null) {
                            GameEngine.LOGGER.warn("Null input detected for player" + index + ".");
                            engine.gameServer.terminateClient(index);
                            characterClass = CharacterClass.DEFAULT;
                        }
                        GameEngine.LOGGER.debug(characterClass);
                        playerClasses.add(characterClass);
                        index++;
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
        while (!ended.stream().allMatch(val -> val = true)) {
            List<String> reads = engine.gameServer.readAll();
            for (int i = 0; i < 4; i++) {
                if (reads.get(i).equals("fin")) {
                    ended.set(i, true);
                }
            }
        }
        engine.gameServer.close();

        File file = new File(output);
        file.getParentFile().mkdirs();

        PrintWriter printWriter = new PrintWriter(file);
        try {
            printWriter.println(engine.log.render());
        } finally {
            printWriter.close();
        }
    }

    /**
     * Executes a given action as an action of the current phase state.
     *
     * @param string Action to be executed as JSON.
     */
    public void execute(String string, int index) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        switch (phaseType) {
            case USE:
                UseAction useAction = mapper.readValue(string, UseAction.class);
                if (useAction == null) {
                    GameEngine.LOGGER.warn("Null input detected for player" + index + ". Removed from game.");
                    gameServer.terminateClient(index);
                    gameState.removePlayer(index);
                    useAction = UseAction.DEFAULT(index);
                } else {
                    index = useAction.getExecutingPlayerIndex();
                }
                gameState.executeUse(useAction);
                uses.set(index, useAction);
                lastActions.set(index, useAction);
                break;
            case MOVE:
                MoveAction moveAction = mapper.readValue(string, MoveAction.class);
                if (moveAction == null) {
                    GameEngine.LOGGER.warn("Null input detected for player" + index + ". Removed from game.");
                    gameServer.terminateClient(index);
                    moveAction = MoveAction.DEFAULT(index);
                } else {
                    index = moveAction.getExecutingPlayerIndex();
                }
                gameState.executeMove(moveAction);
                moves.set(index, moveAction);
                lastActions.set(index, moveAction);
                break;
            case ATTACK:
                AttackAction attackAction = mapper.readValue(string, AttackAction.class);
                if (attackAction == null) {
                    GameEngine.LOGGER.warn("Null input detected for player" + index + ". Removed from game.");
                    gameServer.terminateClient(index);
                    gameState.removePlayer(index);
                    attackAction = AttackAction.DEFAULT(index);
                } else {
                    index = attackAction.getExecutingPlayerIndex();
                }
                gameState.executeAttack(attackAction);
                LOGGER.debug("Printing current health of players");
                for (PlayerState playerState : gameState.getPlayerStateList()) {

                    LOGGER.debug(playerState.getCurrHealth());
                }
                attacks.set(index, attackAction);
                lastActions.set(index, attackAction);
                break;
            case BUY:
                BuyAction buyAction = mapper.readValue(string, BuyAction.class);
                if (buyAction == null) {
                    GameEngine.LOGGER.warn("Null input detected for player" + index + ". Removed from game.");
                    gameServer.terminateClient(index);
                    gameState.removePlayer(index);
                    buyAction = BuyAction.DEFAULT(index);
                } else {
                    index = buyAction.getExecutingPlayerIndex();
                }
                gameState.executeBuy(buyAction);
                buys.set(index, buyAction);
                lastActions.set(index, buyAction);
                break;
        }
    }

    private void start() {
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

    private void beginTurn() {
        gameState.beginTurn();
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
        if (phaseType == GamePhaseType.USE) {
            beginTurn();
        }
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

    private String printBoard() {
        String s = "";
        for (int i = 0; i < Config.BOARD_SIZE; i++) {
            for (int j = 0; j < Config.BOARD_SIZE; j++) {

                int idx = 0;
                boolean found = false;
                for (PlayerState playerState : gameState.getPlayerStateList()) {
                    if (playerState.getPosition().getX() == i && playerState.getPosition().getY() == j) {
                        s += idx;
                        found = true;
                        continue;
                    }
                    idx++;
                }
                if (!found)
                    s += '.';

            }
            s += '\n';
        }
        return s;
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

        //        GameEngine.LOGGER.debug(printBoard());

        int index = 0;
        for (String read : reads) {
            GameEngine.LOGGER.debug(read +", of " + read.getClass());
            execute(read, index);
            index++;
        }
        GameEngine.LOGGER.debug("End phase");
        endPhase();
    }
}
