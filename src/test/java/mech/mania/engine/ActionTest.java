package mech.mania.engine;
import mech.mania.engine.GameState;
import mech.mania.engine.action.MoveAction;
import mech.mania.engine.player.CharacterClass;
import mech.mania.engine.player.PlayerState;
import mech.mania.engine.player.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {

    private PlayerState testPlayer;
    private GameState gameState;

    @BeforeEach
    public void setup() {

        /*
        Configure a default gameState
         */

        List<PlayerState> playerStateList = Arrays.asList(new PlayerState[4]);

        playerStateList.set(0, new PlayerState(CharacterClass.KNIGHT, new Position(0, 0)));
        playerStateList.set(1, new PlayerState());
        playerStateList.set(2, new PlayerState());
        playerStateList.set(3, new PlayerState());

        gameState = new GameState(playerStateList);

        /*
        Set the first player as our test player
         */
        testPlayer = gameState.getPlayerStateByIndex(0);

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

        /*
        VALID: test in range move action
         */
        Position destination = new Position(2, 0);
        MoveAction moveAction = new MoveAction(playerIndex, destination);
        gameState.executeMove(moveAction);

        Position actualPosition = testPlayer.getPosition();

        /*
        assert player's position is your desired position after the movement.
         */
        assertEquals(actualPosition, destination);


        /*
        INVALID: test out of range move action
         */
        destination = new Position(5, 0);
        moveAction = new MoveAction(playerIndex, destination);
        gameState.executeMove(moveAction);

        actualPosition = testPlayer.getPosition();

        assertNotEquals(actualPosition, destination);

        /*
        INVALID: test out of board range move action
         */
        destination = new Position(Config.BOARD_SIZE + 2, 0);
        moveAction = new MoveAction(playerIndex, destination);
        gameState.executeMove(moveAction);

        actualPosition = testPlayer.getPosition();

        assertNotEquals(actualPosition, destination);
    }
}
