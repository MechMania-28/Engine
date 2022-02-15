package mech.mania.engine;

import mech.mania.engine.networking.TestClient;
import mech.mania.engine.networking.TurnServerSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        a. execute use, move, ...
        b. render turn
     */

    Callable<String> server =
        () -> {
          TurnServerSocket turnServer = new TurnServerSocket(27);
          turnServer.write("Hello.");
          System.out.println("here!");
          System.out.println("here!!");
          turnServer.write("Bye.");
          System.out.println("here!!!");

          return "Task's execution";
        };

    Callable<String> client =
        () -> {
          TestClient testClient = new TestClient(27);
          testClient.read();
          System.out.println("here.");

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

    System.out.println("here");
  }
}
