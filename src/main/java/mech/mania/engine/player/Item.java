package mech.mania.engine.player;

public enum Item {
  ;

  /** The buff/debuff StatSet granted by this item. */
  private final StatSet statSet;

  /** Represents whether this item is single-use or not. */
  private final boolean oneTime;

  Item(StatSet statSet, boolean oneTime) {
    this.statSet = statSet;
    this.oneTime = oneTime;
  }

  public boolean isOneTime() {
    return oneTime;
  }

  public StatSet getStatSet() {
    return statSet;
  }
}
