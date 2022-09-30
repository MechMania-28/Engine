package mech.mania.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.tongfei.progressbar.ProgressBar;
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
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private static int turnCount = 0;
    private List<Action> lastActions = Arrays.asList(new Action[4]);

    private static final Logger LOGGER = LogManager.getLogger(GameEngine.class.getName());


    public GameEngine() {
        this.phaseType = null;
        gameServer = new Server();
        this.commState = CommState.START;
        log = new GameLog();
    }


    public static void main(String[] args) throws IOException {


        if (System.getProperty("debug") != null && System.getProperty("debug").equals("true")) {
            Configurator.setLevel(LogManager.getLogger(GameEngine.class).getName(), Level.DEBUG);
            Configurator.setLevel(LogManager.getLogger(Server.class).getName(), Level.DEBUG);
        } else {
            Configurator.setLevel(LogManager.getLogger(GameEngine.class).getName(), Level.INFO);
            Configurator.setLevel(LogManager.getLogger(Server.class).getName(), Level.INFO);
        }

        LOGGER.info("Welcome to Mechmania 28 Engine!");


        String output = System.getProperty("output") == null ?
                "gamelogs\\game_" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + ".json" :
                System.getProperty("output");

        LOGGER.info("Output is set to " + output);

        GameEngine engine = new GameEngine();
        while (!engine.gameServer.isOpen()) engine.gameServer.open();

        ProgressBar progressBar = null;
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
                            GameEngine.LOGGER.info(String.format("Null input detected for player %d at class reporting", index));
                            engine.gameServer.terminateClient(index, engine.turnCount);
                            characterClass = CharacterClass.DEFAULT;
                        }
                        GameEngine.LOGGER.debug(characterClass);
                        playerClasses.add(characterClass);
                        index++;
                    }

                    engine.gameState = new GameState(playerClasses);
                    engine.start();
                    engine.commState = CommState.IN_GAME;
                    progressBar = new ProgressBar("Game Progress", 4 * Config.TURNS);
                    break;
                case IN_GAME:
                    progressBar.step();
                    engine.play();
                    break;
            }
        }

        progressBar.close();
        LOGGER.info("Game ended.");

        LOGGER.info("Writing output...");

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
        try {
            file.getParentFile().mkdirs();
        } catch (NullPointerException e) {
            LOGGER.debug("Cannot create parent directory for given output");
        }

        PrintWriter printWriter = new PrintWriter(file);
        try {
            printWriter.println(engine.log.render());
        } finally {
            printWriter.close();
        }

        LOGGER.info("Completed! Check your output at Engine\\gamelogs.");
    }

    /**
     * Executes a given action as an action of the current phase state.
     *
     * @param string Action to be executed as JSON.
     */
    public void execute(String string, int index) {
        ObjectMapper mapper = new ObjectMapper();
        switch (phaseType) {
            case USE:
                UseAction useAction = null;
                try {
                    useAction = mapper.readValue(string, UseAction.class);
                } catch (IOException e) {
                    GameEngine.LOGGER.debug("Malformed input detected for player" + index + ".");
                    gameServer.terminateClient(index, turnCount);
                    gameState.removePlayer(index);
                    useAction = UseAction.DEFAULT(index);
                }



                if (useAction == null) {
                    GameEngine.LOGGER.debug("Null input detected for player" + index + ".");
                    gameServer.terminateClient(index, turnCount);
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
                MoveAction moveAction = null;
                try {
                    moveAction = mapper.readValue(string, MoveAction.class);
                } catch (IOException e) {
                    GameEngine.LOGGER.debug("Malformed input detected for player" + index + ".");
                    gameServer.terminateClient(index, turnCount);
                    gameState.removePlayer(index);
                    moveAction = MoveAction.DEFAULT(index);
                }
                if (moveAction == null) {
                    GameEngine.LOGGER.debug("Null input detected for player" + index + ".");
                    gameServer.terminateClient(index, turnCount);
                    moveAction = MoveAction.DEFAULT(index);
                } else {
                    index = moveAction.getExecutingPlayerIndex();
                }
                gameState.executeMove(moveAction);


                moves.set(index, moveAction);
                lastActions.set(index, moveAction);
                break;
            case ATTACK:
                AttackAction attackAction = null;
                try {
                    attackAction = mapper.readValue(string, AttackAction.class);
                } catch (IOException e) {
                    GameEngine.LOGGER.debug("Malformed input detected for player" + index + ".");
                    gameServer.terminateClient(index, turnCount);
                    gameState.removePlayer(index);
                    attackAction = AttackAction.DEFAULT(index);
                }
                if (attackAction == null) {
                    GameEngine.LOGGER.debug("Null input detected for player" + index + ".");
                    gameServer.terminateClient(index, turnCount);
                    gameState.removePlayer(index);
                    attackAction = AttackAction.DEFAULT(index);
                } else {
                    index = attackAction.getExecutingPlayerIndex();
                }
                gameState.queueAttack(attackAction);
                attacks.set(index, attackAction);
                lastActions.set(index, attackAction);
                break;
            case BUY:
                BuyAction buyAction = null;
                try {
                    buyAction = mapper.readValue(string, BuyAction.class);
                } catch (IOException e) {
                    GameEngine.LOGGER.debug("Malformed input detected for player" + index + ".");
                    gameServer.terminateClient(index, turnCount);
                    gameState.removePlayer(index);
                    buyAction = BuyAction.DEFAULT(index);
                }
                if (buyAction == null) {
                    GameEngine.LOGGER.debug("Null input detected for player" + index + ".");
                    gameServer.terminateClient(index, turnCount);
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
        if (phaseType == GamePhaseType.USE)
            gameState.beginTurn();


        int index = 0;
        for (String read : reads) {
            GameEngine.LOGGER.debug(read +", of " + read.getClass());
            execute(read, index);

            index++;
        }

        GameEngine.LOGGER.debug("End phase");
        endPhase();
    }

    public static int getTurnCount() {
        return turnCount;
    }
}
