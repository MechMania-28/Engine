package mech.mania.engine;
import mech.mania.engine.GameState;
import mech.mania.engine.player.PlayerState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActionTest {

    private PlayerState playerState;

    @Before
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
