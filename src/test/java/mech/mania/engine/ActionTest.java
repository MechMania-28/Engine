package mech.mania.engine;
import mech.mania.engine.GameState;
import mech.mania.engine.action.MoveAction;
import mech.mania.engine.player.PlayerState;
import mech.mania.engine.player.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionTest {

    private PlayerState playerState;
    private GameState gameState;

    @BeforeEach
    public void setup() {

        GameState gameState = new GameState();
        /*
        create a new player state with some default configurations.
         */
        PlayerState playerState = new PlayerState();


    }

    @Test
    public void sanityCheck() {
        /* sanity check */
        assertEquals(1+1,2);

    }

    @Test
    public void movementTest() {
        /*
        create and execute the moveAction on the player.
         */

        int playerIndex = 0;

        Position destination = new Position(2, 2);
        MoveAction moveAction = new MoveAction(playerIndex, destination);
        gameState.executeMove(moveAction);

        // Implement creation of playerState and gameState to continue.

        // Position actualPosition = gameState.playerStateList[playerIndex].getPosition();

        /*
        assert player's position is your desired position after the movement.
         */
        // assertEquals(actualPosition, destination);

        /*



         */
    }
}
