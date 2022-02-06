package mech.mania.engine.player;

/** Represents the entire state of a Player. */
public class PlayerState {
  private CharacterClass characterClass;
  private Item item;
  private Position position;

  public void setItem(Item item) {
    this.item = item;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  /**
   * Returns the effective {@link StatSet} of the player, defined as the base StatSet of their
   * {@link CharacterClass} and the buff/debuff StatSet of their active {@link Item}.
   *
   * @return Effective StatSet.
   */
  public StatSet getEffectiveStatSet() {
    return characterClass.getStatSet().plus(item.getStatSet());
  }
}
