package mech.mania.engine.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import mech.mania.engine.util.Utility;

/** Represents the entire state of a Player. */
@JsonIgnoreProperties({"currHealth", "dead"})
public class PlayerState {

  private final int index;
  @JsonProperty("isActive")
  private boolean isActive = true;
  @JsonProperty("class")
  private CharacterClass characterClass;

  @JsonProperty("item")
  private Item itemHolding = Item.NONE;

  @JsonProperty("position")
  private Position position;

  @JsonProperty("gold")
  private int gold;

  @JsonProperty("score")
  private int score;

  @JsonIgnore private int effectTimer;

  @JsonProperty("health")
  private int health;

  @JsonProperty("shielded")
  private boolean shielded;

  private Item itemInEffect = Item.NONE;

  @JsonCreator
  public PlayerState(
      @JsonProperty("class") CharacterClass characterClass,
      @JsonProperty("position") Position position,
      int index) {
    this.characterClass = characterClass;
    this.position = position;
    this.health = characterClass.getStatSet().getMaxHealth();
    this.index = index;
  }


  public Item getItemHolding() {
    return this.itemHolding;
  }

  public void setItemHolding(Item itemHolding) {
    this.itemHolding = itemHolding;
  }

  public CharacterClass getCharacterClass() {
    return this.characterClass;
  }

  public void setCharacterClass(CharacterClass characterClass) {
    this.characterClass = characterClass;
  }

  public int getGold() {
    return gold;
  }

  public void setGold(int amount) {
    gold = amount;
  }

  public void incrementGold(int amount) {
    gold += amount;
  }

  public void decrementGold(int amount) {
    gold -= amount;
    if (gold < 0) gold = 0;
  }

  @JsonProperty("effect_timer")
  public int getEffectTimer() {
    return this.effectTimer;
  }

  public void setEffectTimer(int effectTimer) {
    this.effectTimer = effectTimer;
  }

  public void decrementEffectTimer() {
    this.effectTimer --;
  }

  public void incrementCurrHealth(int amount) {

    this.health += amount;
    if (health > this.getEffectiveStatSet().getMaxHealth()) {
      this.health = this.getEffectiveStatSet().getMaxHealth();
    }
    if (health < 0) {
      this.health = 0;
    }

  }

  public void setShielded(boolean shielded) {
    this.shielded = shielded;
  }

  public boolean isShielded() {
    return shielded;
  }

  public void checkAndHandleDeath(int index) {
    if (health == 0) {
      position = Utility.spawnPoints.get(index);
    }
  }

  public void refreshHealth() {
    incrementCurrHealth(0);
  }

  public void checkAndHandleBase(int index) {
    if (position.equals(Utility.spawnPoints.get(index))) {
      health = getEffectiveStatSet().getMaxHealth();
    }
  }

  /**
   * Returns the effective {@link StatSet} of the player, defined as the base StatSet of their
   * {@link CharacterClass} and the buff/debuff StatSet of their active {@link Item}.
   *
   * @return Effective StatSet.
   */
  @JsonProperty("stat_set")
  public StatSet getEffectiveStatSet() {
    // Item is either permanent or the buff is still in effect
    if (this.effectTimer > 0) {
      return characterClass.getStatSet().plus(itemInEffect.getStatSet());
    } else if (this.effectTimer < 0) {

      return itemHolding.isPermanent()
              ? characterClass.getStatSet().plus(itemHolding.getStatSet())
              : characterClass.getStatSet();
    }
    else {
      // No effective item
      return characterClass.getStatSet();
    }
  }


  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public void incrementScore() {
    score++;
  }

  public int getCurrHealth() {
    return health;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public boolean isDead() {
    return health == 0;
  }

  public void useItem() {
    setItemInEffect(itemHolding);
    setEffectTimer(itemHolding.getEffectTimer());

    setItemHolding(Item.NONE);

  }

  @JsonProperty("item_in_use")
  public Item getItemInEffect() {
    return itemInEffect;
  }

  public void setItemInEffect(Item item) {
    this.itemInEffect = item;
  }
}
