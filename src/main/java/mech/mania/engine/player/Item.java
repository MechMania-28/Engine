package mech.mania.engine.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown=true)
public enum Item {
  SHIELD(new StatSet(0, 0, 0, 0), 1, 8) {
    @Override
    public void affect(PlayerState player) {
      player.setShielded(true);
    }
  },
  PROCRUSTEAN_IRON(new StatSet(0, 0, 0, 0), -1, 8) {
    @Override
    public void affect(PlayerState player) {
    }
  },
  HEAVY_BROADSWORD(new StatSet(0, 0, 0, 0), 0, 8) {
    @Override
    public void affect(PlayerState player) {
      player.setCharacterClass(CharacterClass.KNIGHT);
    }
  },
  MAGIC_STAFF(new StatSet(0, 0, 0, 0), 0, 8) {
    @Override
    public void affect(PlayerState player) {
      player.setCharacterClass(CharacterClass.WIZARD);
    }
  },
  STEEL_TIPPED_ARROW(new StatSet(0, 0, 0, 0), 0, 8) {
    @Override
    public void affect(PlayerState player) {
      player.setCharacterClass(CharacterClass.ARCHER);
    }
  },
  ANEMOI_WINGS(new StatSet(0, 0, 2, 0), -1, 8) {
    @Override
    public void affect(PlayerState player) {
      // do nothing
      }
  },
  HUNTER_SCOPE(new StatSet(0, 0, 0, 2), -1, 8) {
    @Override
    public void affect(PlayerState player) {

    }
  },
  STRENGTH_POTION(new StatSet(0, 4, 0, 0), 1, 5) {
    @Override
    public void affect(PlayerState player) {

    }
  },
  SPEED_POTION(new StatSet(0, 0, 2, 0), 1, 5 ) {
    @Override
    public void affect(PlayerState player) {

    }
  },
  DEXTERITY_POTION(new StatSet(0, 0, 0, 2), 1, 5 ) {
    @Override
    public void affect(PlayerState player) {

    }
  },



  NONE(new StatSet(0, 0, 0, 0), -1, 100) {
    @Override
    public void affect(PlayerState player) {
      // Do nothing
    }
  };




  /** The effect the item has on a player */
  public abstract void affect(PlayerState player);

  /** The buff/debuff StatSet granted by this item. */
  private final StatSet statSet;

  /** Represents the duration of this item's effect
   * -1 means permanent, 0 means one time, positive means multiple turns*/
  private final int effectTimer;

  /** Represents the cost of the item */
  private final int cost;

  Item(StatSet statSet, int itemTimer, int cost) {
    this.statSet = statSet;
    this.effectTimer = itemTimer;
    this.cost = cost;
  }

  public StatSet getStatSet() {
    return statSet;
  }

  public int getEffectTimer() {
    return effectTimer;
  }

  public boolean isPermanent() {
    return (effectTimer == -1);
  }

  public int getCost() {
    return cost;
  }
}
