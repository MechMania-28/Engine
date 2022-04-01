package mech.mania.engine.action;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public abstract class Action {
  @JsonProperty("executor")
  private final int executingPlayerIndex;

  public Action(int executingPlayerIndex) {
    this.executingPlayerIndex = executingPlayerIndex;
  }
  public int getExecutingPlayerIndex() {
    return executingPlayerIndex;
  }

}
