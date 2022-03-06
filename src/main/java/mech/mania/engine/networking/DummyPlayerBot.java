package mech.mania.engine.networking;

import mech.mania.engine.player.CharacterClass;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DummyPlayerBot {
  private final Client gameClient;
  private CommState commState;

  public DummyPlayerBot(int gamePort) {
    this.gameClient = new Client(gamePort);
    this.commState = CommState.START;
  }

  public static void main(String[] args) {
    List<Integer> ports = Arrays.stream(args).map(Integer::valueOf).collect(Collectors.toList());
    int enginePort = ports.get(0);

    DummyPlayerBot bot = new DummyPlayerBot(enginePort);
    while (!bot.gameClient.isConnected()) bot.gameClient.connect();

    while (bot.commState != CommState.END) {
      switch (bot.commState) {
        case START:
          while (!bot.gameClient.read().equals("wake")) continue;
          bot.commState = CommState.NUM_ASSIGN;
          break;
        case NUM_ASSIGN:
          // Wait for a single digit number.
          String read = bot.gameClient.read();
          //          if (!read.matches("\\d")) continue;
          System.out.println(read);
          bot.commState = CommState.CLASS_REPORT;
          break;
        case CLASS_REPORT:
          bot.gameClient.write(
              CharacterClass.values()[new Random().nextInt(CharacterClass.values().length)]);
          bot.commState = CommState.IN_GAME;
          break;
        case IN_GAME:
          bot.commState = CommState.END;
          break;
      }
    }

    while (!bot.gameClient.read().equals("fin")) continue;
    bot.gameClient.write("fin");
    bot.gameClient.disconnect();
  }


}
