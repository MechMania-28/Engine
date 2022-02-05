package mech.mania.engine;

public enum CharacterClass {
  KNIGHT(9,6,2,1),
  WIZARD(6,4,3,2),
  ARCHER(3,2,4,3);

  private int maxHealth;
  private int damage;
  private int speed;
  private int range;

  CharacterClass(int maxHealth, int damage, int speed, int range) {
    this.maxHealth = maxHealth;
    this.damage = damage;
    this.speed = speed;
    this.range = range;
  }

  public int getDamage() {
    return damage;
  }

  public int getRange() {
    return range;
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  public int getSpeed() {
    return speed;
  }
}
