package mech.mania.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class GameLog {
  private List<GamePhase> phases  = new ArrayList<>();
  public void addPhase(GamePhase phase){
    phases.add(phase);
  }
  public String render() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(phases);
  }
}
