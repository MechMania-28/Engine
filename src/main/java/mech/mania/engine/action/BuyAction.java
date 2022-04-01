package mech.mania.engine.action;

import mech.mania.engine.player.Item;

public class BuyAction extends Action {

  private final Item item;

  public BuyAction(int executingPlayerIndex, Item item) {
    super(executingPlayerIndex);
    this.item = item;
  }

  public Item getItem() {
    return item;
  }

}
