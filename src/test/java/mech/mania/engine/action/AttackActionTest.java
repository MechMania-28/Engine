package mech.mania.engine.action;

import mech.mania.engine.*;
import java.util.Arrays;
import java.util.List;


import mech.mania.engine.player.CharacterClass;
import mech.mania.engine.player.Item;
import mech.mania.engine.player.PlayerState;
import mech.mania.engine.player.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static mech.mania.engine.Config.BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.*;


public class AttackActionTest {

    private PlayerState testP1;
    private PlayerState testP2;
    private PlayerState testP3;
    private GameState gameState;

    @BeforeEach
    public void setup() {
        // Configure a default gameState

        List<PlayerState> playerStateList = Arrays.asList(new PlayerState[4]);
        playerStateList.set(0, new PlayerState(CharacterClass.KNIGHT, new Position(0, 0)));
        playerStateList.set(1, new PlayerState(CharacterClass.ARCHER, new Position(1, 1)));
        playerStateList.set(2, new PlayerState(CharacterClass.WIZARD, new Position(2, 0)));
        playerStateList.set(3, new PlayerState());

        gameState = new GameState(playerStateList);

        testP1 = gameState.getPlayerStateByIndex(0);
        testP2 = gameState.getPlayerStateByIndex(1);
        testP3 = gameState.getPlayerStateByIndex(2);
        testP1.setItem(Item.NULL_ITEM);
        testP2.setItem(Item.NULL_ITEM);
        testP3.setItem(Item.NULL_ITEM);
    }

    @Test
    /* Test a player successfully attacking another player  */
    public void attackPlayerTest() {


        // Create new AttackAction
        AttackAction attackAction = new AttackAction(1, 0);
        gameState.executeAttack(attackAction);

        // Test that the target has lost health correctly
        assertEquals(testP1.getCurrHealth(), testP1.getEffectiveStatSet().getMaxHealth() - testP2.getEffectiveStatSet().getDamage());

    }

    @Test
    /* Test a player attacking out of range */
    public void outOfRangeTest() {
        // Create new AttackAction
        AttackAction attackAction = new AttackAction(0, 1);
        gameState.executeAttack(attackAction);
        assertEquals(testP2.getCurrHealth(), testP2.getEffectiveStatSet().getMaxHealth());
    }

    @Test
    /* Test a player taking multiple hits in one turn */
    public void multipleHitsTest() {
        // Create new attackActions
        AttackAction attackOne = new AttackAction(1, 0);
        AttackAction attackTwo = new AttackAction(2, 0);
        gameState.executeAttack(attackOne);
        gameState.executeAttack(attackTwo);
        assertEquals(testP1.getCurrHealth(), testP1.getEffectiveStatSet().getMaxHealth()-6); // Wizard damage + archer damage
    }


    @Test
    /* Test multiple hits over a series of turns */
    public void multipleTurnsTest() {

    }


    @Test
    /* Test Item.PROCRUSTEAN_IRON */
    public void flatDamageTest() {
        testP1.setItem(Item.PROCRUSTEAN_IRON);
        AttackAction attackOne = new AttackAction(1, 0);
        gameState.executeAttack(attackOne);
        assertEquals(testP1.getCurrHealth(), testP1.getEffectiveStatSet().getMaxHealth() - 4); // wizard damage

        AttackAction attackTwo = new AttackAction(2, 0);
        gameState.executeAttack(attackTwo);
        assertEquals(testP1.getCurrHealth(), testP1.getEffectiveStatSet().getMaxHealth() - 8);

    }
}



