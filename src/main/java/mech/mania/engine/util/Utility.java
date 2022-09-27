package mech.mania.engine.util;

import mech.mania.engine.Config;
import mech.mania.engine.player.PlayerState;
import mech.mania.engine.player.Position;

public class Utility {

  public static boolean onControlTile(Position position) {
    System.out.println((Config.BOARD_SIZE - 1) / 2);
    int controlTileBegin = (Config.BOARD_SIZE - 1) / 2;
    int controlTileEnd = controlTileBegin + 1;
    return (controlTileBegin <= position.getX() && position.getX() <= controlTileEnd)
            && (controlTileBegin <= position.getY() && position.getY() <= controlTileEnd);
  }
  public static int manhattanDistance(Position p1, Position p2) {
    return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
  }


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
