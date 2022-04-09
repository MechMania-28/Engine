package mech.mania.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class GameLog {
  private List<String> turns = new ArrayList<>();
  public void addTurn(GameTurn turn){
    ObjectMapper mapper = new ObjectMapper();
    try {
      turns.add(mapper.writeValueAsString(turn));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
  public String render() throws JsonProcessingException {
    return "["+String.join("," ,turns)+"]";
  }
}
