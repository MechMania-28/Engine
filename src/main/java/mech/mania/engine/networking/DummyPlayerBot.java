package mech.mania.engine.networking;

import com.fasterxml.jackson.databind.ObjectMapper;
import mech.mania.engine.GamePhase;
import mech.mania.engine.GamePhaseType;
import mech.mania.engine.action.AttackAction;
import mech.mania.engine.action.BuyAction;
import mech.mania.engine.action.MoveAction;
import mech.mania.engine.action.UseAction;
import mech.mania.engine.player.CharacterClass;
import mech.mania.engine.player.Item;
import mech.mania.engine.player.Position;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DummyPlayerBot {
  private final Client gameClient;
  private CommState commState;
  private int playerNum;

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
          bot.playerNum = Integer.parseInt(bot.gameClient.read());
          bot.commState = CommState.CLASS_REPORT;
          break;
        case CLASS_REPORT:
          bot.gameClient.write(
              CharacterClass.values()[new Random().nextInt(CharacterClass.values().length)]);
          bot.commState = CommState.IN_GAME;
          break;
        case IN_GAME:
          try {
            bot.play();
          } catch (IOException e) {
            e.printStackTrace();
          }
          break;
      }
    }

    bot.gameClient.write("fin");
    bot.gameClient.disconnect();
  }

  public void play() throws IOException {
    String read = gameClient.read();
    if (read.equals("fin")) {
      commState = CommState.END;
      return;
    }
    System.out.println(read);
    GamePhase gamePhase = new ObjectMapper().readValue(read, GamePhase.class);
    if (gamePhase.next_phase == GamePhaseType.USE) {
      UseAction action = new UseAction(playerNum, false);
      gameClient.write(action);
    }
    else if (gamePhase.next_phase == GamePhaseType.MOVE) {
      MoveAction action = new MoveAction(playerNum, new Position(1,1));
      gameClient.write(action);
    }
    else if (gamePhase.next_phase==GamePhaseType.ATTACK){
      gameClient.write(new AttackAction(playerNum, 2));
    }
    else if (gamePhase.next_phase==GamePhaseType.BUY)
      gameClient.write(new BuyAction(playerNum, Item.ANEMOI_WINGS));

  }
}
