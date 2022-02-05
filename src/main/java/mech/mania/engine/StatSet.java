package mech.mania.engine;

public class StatSet {
  private int maxHealth;
  private int damage;
  private int speed;
  private int range;

  public StatSet(int maxHealth, int damage, int speed, int range) {
    this.maxHealth = maxHealth;
    this.damage = damage;
    this.speed = speed;
    this.range = range;
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  public int getDamage() {
    return damage;
  }

  public int getSpeed() {
    return speed;
  }

  public int getRange() {
    return range;
  }
}
