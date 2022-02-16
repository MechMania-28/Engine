package mech.mania.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import mech.mania.engine.networking.Client;
import mech.mania.engine.networking.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  public static void main(String[] args) {
    /**
     * A single turn: - Use - Move - Attack - Buy
     *
     * <p>DecisionSet becomes a compilation of these 4. At the end of a turn, decision_set is
     * written to JSON.
     *
     * <p>Turn { players: [ Player { // state } ], decision_sets: [ DecisionSet { use, move, attack,
     * buy } ] }
     */

    /*
    1. Create GameState
    2. For each turn of game,
        a. execute use, move, ...ser
        b. render turn
     */
    //    final int maxTurns = 10;
    //    GameState state = new GameState();
    //    for (int i = 0; i < maxTurns; i++) {
    //      state.executeUse(new UseAction());
    //      state.executeMove(new MoveAction());
    //      state.executeAttack(new AttackAction());
    //      state.executeBuy(new BuyAction());
    //    }

    Callable<String> server =
        () -> {
          Server turnServer = new Server(27);
          while (!turnServer.isStarted()) turnServer.start();
          turnServer.write("Hello.");
          turnServer.write("Bye.");
          turnServer.write(new GameTurn());
          return "Task's execution";
        };

    Callable<String> client =
        () -> {
          Client testClient = new Client(27);
          while (!testClient.isConnected()) testClient.connect();
          System.out.println(testClient.read());
          System.out.println(testClient.read());
          String turn = testClient.read();
          System.out.println(turn);
          System.out.println(new GameTurn().equals(new ObjectMapper().readValue(turn, GameTurn.class)));
          return "Task's execution";
        };

    List<Callable<String>> callableTasks = new ArrayList<>();
    callableTasks.add(server);
    callableTasks.add(client);

    ExecutorService executor = Executors.newFixedThreadPool(2);
    try {
      executor.invokeAll(callableTasks);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    executor.shutdown();
  }
}
