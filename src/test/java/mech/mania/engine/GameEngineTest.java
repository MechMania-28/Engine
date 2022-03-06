package mech.mania.engine;

import mech.mania.engine.action.UseAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class GameEngineTest {

  private GameEngine engine;

  @BeforeEach
  public void setup() {
    this.engine = new GameEngine(0);
  }

  @Test
  public void testPhaseChange() {
    for (int i = 0; i < 4; i++) {
      Assertions.assertEquals(engine.getPhase(), GamePhaseType.USE);
      engine.execute(new UseAction(i));
    }
    Assertions.assertEquals(engine.getPhase(), GamePhaseType.MOVE);
  }
}
