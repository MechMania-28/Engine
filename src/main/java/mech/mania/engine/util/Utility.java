package mech.mania.engine.util;

import mech.mania.engine.Config;
import mech.mania.engine.player.PlayerState;
import mech.mania.engine.player.Position;

import java.util.Arrays;
import java.util.List;

import static mech.mania.engine.Config.BOARD_SIZE;

public class Utility {

  public static boolean onControlTile(Position position) {
    int controlTileBegin = (Config.BOARD_SIZE - 1) / 2;
    int controlTileEnd = controlTileBegin + 1;
    return (controlTileBegin <= position.getX() && position.getX() <= controlTileEnd)
            && (controlTileBegin <= position.getY() && position.getY() <= controlTileEnd);
  }
  public static int manhattanDistance(Position p1, Position p2) {
    return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
  }

  public static final List<Position> spawnPoints = Arrays.asList(
          new Position(0, 0),
          new Position(BOARD_SIZE-1, 0),
          new Position(BOARD_SIZE-1, BOARD_SIZE-1),
          new Position(0, BOARD_SIZE-1)
  );


  public static boolean inBounds(Position p) {
    // Assume board runs from 0 to BOARD_SIZE - 1
    return ((p.getX() >= 0)
        && (p.getX() < Config.BOARD_SIZE)
        && (p.getY() >= 0)
        && (p.getY() < Config.BOARD_SIZE));
  }

  public static int squareDistance(Position p1, Position p2) {
    return Math.max(Math.abs(p1.getX()-p2.getX()), Math.abs(p1.getY()- p2.getY()));
  }
}
