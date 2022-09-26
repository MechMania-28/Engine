package mech.mania.engine.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Position {
  private int x;
  private int y;

  @JsonCreator
  public Position(@JsonProperty("x") int x, @JsonProperty("y") int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void translate(Position destination) {
    this.x = destination.getX();
    this.y = destination.getY();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Position)) {
      return false;
    }
    Position p = (Position) obj;
    // If the individual coordinates are the same
    return ((this.getX() == p.getX()) & (this.getY() == p.getY()));
  }
}
