package mech.mania.engine.action;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
//@JsonDeserialize(as = UseAction.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {

  @JsonProperty("isValid")
  private boolean isValid = true;

  public void invalidate() {
    isValid = false;
  }

  @JsonProperty("executor")
  private final int executingPlayerIndex;

  @JsonCreator
  public Action(@JsonProperty("executor")int executingPlayerIndex) {
    this.executingPlayerIndex = executingPlayerIndex;
  }
  public int getExecutingPlayerIndex() {
    return executingPlayerIndex;
  }

}
