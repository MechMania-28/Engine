package mech.mania.engine;

import mech.mania.engine.Config;

public class Utility {
    public static int manhattanDistance(int x0, int x1, int y0, int y1){
        return Math.abs(x1 - x0) + Math.abs(y1 - y0);
    }
    public static boolean inBounds(int x, int y){
        /** Assume board runs from 0 to BOARD_SIZE - 1 */
        return ((x >= 0) && ( x < Config.BOARD_SIZE) && (y >= 0) && (y < Config.BOARD_SIZE));
    }
}
