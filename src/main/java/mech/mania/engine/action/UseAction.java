package mech.mania.engine.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UseAction extends Action {

  public static UseAction DEFAULT(int playerIndex) {
    return new UseAction(playerIndex, false);
  }
  @JsonCreator
  public UseAction(@JsonProperty("executor")int executingPlayerIndex, @JsonProperty("use") boolean use) {
    super(executingPlayerIndex);
    this.use = use;
  }

  @JsonProperty("use")
  private final boolean use;

  public boolean shouldUse() {
    return use;
  }


}
