package mech.mania.engine;

public class Utility {
    public static int manhattanDistance(int x0, int x1, int y0, int y1){
        return Math.abs(x1 - x0) + Math.abs(y1 - y0);
    }
    public static boolean inBounds(int x, int y){
        return ((x >= 0) && ( x <= 9) && (y >= 0) && (y <= 9 ));
    }
}
