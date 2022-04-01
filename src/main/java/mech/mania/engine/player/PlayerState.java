package mech.mania.engine.player;

/** Represents the entire state of a Player. */
public class PlayerState {
  @JsonProperty("class")
  private CharacterClass characterClass;

  @JsonProperty("item")
  private Item item = Item.NONE;

  @JsonProperty("position")
  private Position position;

  @JsonProperty("gold")
  private int gold;

  @JsonProperty("score")
  private int score;

  @JsonIgnore private int effectTimer;

  @JsonProperty("health")
  private int health;

  @JsonCreator
  public PlayerState(
      @JsonProperty("class") CharacterClass characterClass,
      @JsonProperty("position") Position position) {
    this.characterClass = characterClass;
    this.position = position;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public Item getItem() {
    return this.item;
  }

  public void setCharacterClass(CharacterClass characterClass) {
    this.characterClass = characterClass;
  }

  public CharacterClass getCharacterClass() {
    return this.characterClass;
  }

  public int getGold() {
    return gold;
  }

  public void setGold(int amount) {
    this.gold = amount;
  }

  public void incrementGold(int amount) {
    this.gold += amount;
  }

  public int getEffectTimer() {
    return this.effectTimer;
  }

  public void setEffectTimer(int effectTimer) {
    this.effectTimer = effectTimer;
  }

  public void decrementEffectTimer(int amount) {
    this.effectTimer -= amount;
  }

  public int getCurrHealth() {
    return currHealth;
  }

  public void setCurrHealth(int currHealth) {
    this.currHealth = currHealth;
  }

  public void incrementCurrHealth(int amount) {
    this.currHealth += amount;
    if (currHealth > this.computeEffectiveStatSet().getMaxHealth()) {
      this.setCurrHealth(this.computeEffectiveStatSet().getMaxHealth());
    }
  }

  /**
   * Returns the effective {@link StatSet} of the player, defined as the base StatSet of their
   * {@link CharacterClass} and the buff/debuff StatSet of their active {@link Item}.
   *
   * @return Effective StatSet.
   */
  public StatSet computeEffectiveStatSet() {
    // Item is either permanent or the buff is still in effect
    if (this.effectTimer != 0) {
      return characterClass.getStatSet().plus(item.getStatSet());
    }
    // No effective item
    else {
      return characterClass.getStatSet();
    }
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }
}
