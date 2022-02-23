package mech.mania.engine.player;

/** Represents the entire state of a Player. */
public class PlayerState {
  private CharacterClass characterClass;
  private Item item;
  private Position position;

  public PlayerState(CharacterClass characterClass, Position position) {
    this.characterClass = characterClass;
    this.position = position;
  }

  public PlayerState() {

  }

  public void setItem(Item item) {
    this.item = item;
  }

  /**
   * Returns the effective {@link StatSet} of the player, defined as the base StatSet of their
   * {@link CharacterClass} and the buff/debuff StatSet of their active {@link Item}.
   *
   * @return Effective StatSet.
   */
  public StatSet getEffectiveStatSet() {
    if (item != null) {
      return characterClass.getStatSet().plus(item.getStatSet());
    }
    else {
      return characterClass.getStatSet();
    }
  }

  public Position getPosition() {
    return position;
  }
}
