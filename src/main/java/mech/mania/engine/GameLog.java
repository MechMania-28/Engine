package mech.mania.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameLog {

  GameLog() {
    names = Arrays.asList(new String[]{
            System.getProperty("bot0") != null ? System.getProperty("bot0") : "bot0",
            System.getProperty("bot1") != null ? System.getProperty("bot1") : "bot1",
            System.getProperty("bot2") != null ? System.getProperty("bot2") : "bot2",
            System.getProperty("bot3") != null ? System.getProperty("bot3") : "bot3"
    }).stream().map(name -> "\"" + name + "\"").collect(Collectors.toList());
  }

  private List<String> names;
  private List<String> turns = new ArrayList<>();
  public void addTurn(GameTurn turn){
    ObjectMapper mapper = new ObjectMapper();
    try {
      turns.add(mapper.writeValueAsString(turn));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public String render() {
    String turnsString = "[" + String.join(","  , turns) + "]";
    String namesString = "[" + String.join(",", names) + "]";
    return "{\"names\":" + namesString + ", \"turns\":" + turnsString + "}";
  }
}
