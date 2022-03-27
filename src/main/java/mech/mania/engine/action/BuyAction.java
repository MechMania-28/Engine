package mech.mania.engine.action;

import mech.mania.engine.player.Item;

public class BuyAction extends Action {

  private Item item;

  public BuyAction(int executingPlayerIndex) {
    super(executingPlayerIndex);
  }

  public Item getItem() {
    return item;
  }

}
