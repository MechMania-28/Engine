package mech.mania.engine.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UseAction extends Action {
  @JsonCreator
  public UseAction(@JsonProperty("executor")int executingPlayerIndex) {
    super(executingPlayerIndex);
  }
}
