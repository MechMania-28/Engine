package mech.mania.engine.action;
import mech.mania.engine.Config;
import mech.mania.engine.GameState;
import mech.mania.engine.action.MoveAction;
import mech.mania.engine.player.CharacterClass;
import mech.mania.engine.player.PlayerState;
import mech.mania.engine.player.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MoveActionTest {

    private PlayerState testPlayer;
    private GameState gameState;

    @BeforeEach
    public void setup() {

        /*
        Configure a default gameState
         */

        List<CharacterClass> playerStateList = Arrays.asList(new CharacterClass[4]);

        playerStateList.set(0, CharacterClass.KNIGHT);
        playerStateList.set(1, CharacterClass.KNIGHT);
        playerStateList.set(2, CharacterClass.KNIGHT);
        playerStateList.set(3, CharacterClass.KNIGHT);

        gameState = new GameState(playerStateList);

        /*
        Set the first player as our test player
         */
        testPlayer = gameState.getPlayerStateByIndex(0);

    }


    @Test
    public void moveInPlayerRange() {
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
    }

    @Test
    public void teleportBackTest() {
        testPlayer.setPosition(new Position(9, 9));
        MoveAction moveAction = new MoveAction(0, new Position(0, 0));
        gameState.executeMove(moveAction);
        System.out.println("" + testPlayer.getPosition().getX() + " " + testPlayer.getPosition().getY() );

    }

    @Test
    public void moveOutPlayerRange() {

        int playerIndex = 0;

        /*
        INVALID: test out of range move action
         */
        Position destination = new Position(5, 0);
        MoveAction moveAction = new MoveAction(playerIndex, destination);
        gameState.executeMove(moveAction);

        Position actualPosition = testPlayer.getPosition();

        System.out.println(String.format("%d, %d", actualPosition.getX(), actualPosition.getY()));
        System.out.println(String.format("%d, %d", destination.getX(), destination.getY()));

        assertNotEquals(actualPosition, destination);
    }
    @Test
    public void moveOutBoardRange() {

        int playerIndex = 0;
        /*
        INVALID: test out of board range move action
         */
        Position destination = new Position(Config.BOARD_SIZE + 2, 0);
        MoveAction moveAction = new MoveAction(playerIndex, destination);
        gameState.executeMove(moveAction);

        Position actualPosition = testPlayer.getPosition();

        System.out.println(String.format("%d, %d", actualPosition.getX(), actualPosition.getY()));
        System.out.println(String.format("%d, %d", destination.getX(), destination.getY()));

        assertNotEquals(actualPosition, destination);

    }
}
