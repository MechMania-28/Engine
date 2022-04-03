package mech.mania.engine.util;

import mech.mania.engine.Config;
import mech.mania.engine.player.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilityTest {

  @Test
  void manhattanDistance() {
    Assertions.assertEquals(Utility.manhattanDistance(new Position(0, 0), new Position(3, 4)), 7);
    Assertions.assertEquals(Utility.manhattanDistance(new Position(3, 4), new Position(0, 0)), 7);
    Assertions.assertEquals(Utility.manhattanDistance(new Position(3, 4), new Position(3, 4)), 0);
  }

  @Test
  void squareDistance() {
    Assertions.assertEquals(Utility.squareDistance(new Position(0, 0), new Position(1, 1)), 1);
    Assertions.assertEquals(Utility.squareDistance(new Position(0, 0), new Position(2, 1)), 2);
    Assertions.assertEquals(Utility.squareDistance(new Position(1, 2), new Position(0, 0)), 2);
  }
  @Test
  void inBounds() {
    Assertions.assertTrue(Utility.inBounds(new Position(0,0)));
    Assertions.assertTrue(Utility.inBounds(new Position(Config.BOARD_SIZE - 1,Config.BOARD_SIZE-1)));
    Assertions.assertTrue(Utility.inBounds(new Position(0,Config.BOARD_SIZE-1)));
    Assertions.assertTrue(Utility.inBounds(new Position(Config.BOARD_SIZE - 1,0)));

    Assertions.assertFalse(Utility.inBounds(new Position(0,Config.BOARD_SIZE+1)));
    Assertions.assertFalse(Utility.inBounds(new Position(Config.BOARD_SIZE+1,0)));
    Assertions.assertFalse(Utility.inBounds(new Position(0,-1)));
    Assertions.assertFalse(Utility.inBounds(new Position(-1,0)));
  }
}
