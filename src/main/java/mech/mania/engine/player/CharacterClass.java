package mech.mania.engine.player;

public enum CharacterClass {
  KNIGHT(9, 6, 2, 1),
  WIZARD(6, 4, 3, 2),
  ARCHER(3, 2, 4, 3);

  public static final CharacterClass DEFAULT = KNIGHT;
  private final StatSet statSet;

  CharacterClass(int maxHealth, int damage, int speed, int range) {
    this.statSet = new StatSet(maxHealth, damage, speed, range);
  }

  public StatSet getStatSet() {
    return statSet;
  }
}
