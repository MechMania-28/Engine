package mech.mania.engine;

public class Main {
  public static void main(String[] args){
    /**
     * A single turn:
     * - Use
     * - Move
     * - Attack
     * - Buy
     *
     * DecisionSet becomes a compilation of these 4. At the end of a turn, decision_set is written to JSON.
     *
     * Turn {
     * players: [
     *  Player {
     *    // state
     *  }
     * ],
     * decision_sets: [
     *  DecisionSet {
     *    use, move, attack, buy
     *  }
     * ]
     * }
     */

    /*
    1. Create GameState
    2. For each turn of game,
        a. execute use, move, ...
        b. render turn
     */
  }
}
