package mech.mania.engine.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BuyAction extends Action {
  @JsonCreator
  public BuyAction(@JsonProperty("executor") int executingPlayerIndex) {
    super(executingPlayerIndex);
  }
}
