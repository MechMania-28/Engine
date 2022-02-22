package mech.mania.engine;

/**
 * The possible phases the game might be in at any given time, in order of gameplay.
 */
public enum GamePhaseType {
  USE,MOVE,ATTACK,BUY;

  /**
   * Returns the next phase iterated in order of gameplay.
   * @return The next phase.
   */
  public GamePhaseType next() {
    // No bounds checking required here, because the last instance overrides
    return values()[(ordinal() + 1) % values().length];
  }

}
