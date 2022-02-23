package mech.mania.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GamePhaseTypeTest {

  @Test
  void next() {
    GamePhaseType type = GamePhaseType.USE;
    Assertions.assertEquals(type.next(), GamePhaseType.MOVE);
    Assertions.assertEquals(type.next().next(), GamePhaseType.ATTACK);
    Assertions.assertEquals(type.next().next().next(), GamePhaseType.BUY);
    Assertions.assertEquals(type.next().next().next().next(), GamePhaseType.USE);
  }
}
