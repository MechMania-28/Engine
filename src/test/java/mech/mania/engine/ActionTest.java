package mech.mania.engine;
import mech.mania.engine.GameState;
import mech.mania.engine.player.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionTest {

    private PlayerState playerState;

    @BeforeEach
    public void setup() {
        /*
        create a new player state with some default configurations.
         */
    }

    @Test
    public void sanityCheck() {
        /*
        create and excecute the moveaction on the player.
         */
        assertEquals(1+1,2);
        /*
        assert player's position is your desired position after the movement.
         */
    }
}
