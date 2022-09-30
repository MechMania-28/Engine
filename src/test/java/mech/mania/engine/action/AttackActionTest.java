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

        List<CharacterClass> playerStateList = Arrays.asList(new CharacterClass[4]);
        playerStateList.set(0, CharacterClass.KNIGHT);
        playerStateList.set(1, CharacterClass.ARCHER);
        playerStateList.set(2, CharacterClass.KNIGHT);
        playerStateList.set(3, CharacterClass.KNIGHT);

        gameState = new GameState(playerStateList);

        testP1 = gameState.getPlayerStateByIndex(0);
        testP2 = gameState.getPlayerStateByIndex(1);
        testP3 = gameState.getPlayerStateByIndex(2);
        testP1.setItemHolding(Item.NONE);
        testP1.setPosition(new Position(0,0));
        testP2.setItemHolding(Item.NONE);
        testP2.setPosition(new Position(0,0));
        testP3.setItemHolding(Item.NONE);
        testP3.setPosition(new Position(0,0));

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
        testP1.getPosition().translate(new Position(BOARD_SIZE-1, BOARD_SIZE-1));
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
        assertEquals(testP1.getCurrHealth(), testP1.getEffectiveStatSet().getMaxHealth()-8); // Wizard damage (6) + archer damage(2)
    }

    @Test
    public void checkShieldTest() {

        System.out.println(testP1.getCurrHealth());

        gameState.getPlayerStateByIndex(0).setItemHolding(Item.SHIELD);

        gameState.beginTurn();

        gameState.executeUse(new UseAction(0, true));

        AttackAction attackAction1 = new AttackAction(1, 0);
        AttackAction attackAction2 = new AttackAction(2, 0);

        System.out.println(gameState.getPlayerStateByIndex(0).getItemInEffect());

        gameState.queueAttack(attackAction1);
        gameState.queueAttack(attackAction1);
        gameState.queueAttack(attackAction1);
        gameState.queueAttack(attackAction2);

        gameState.endTurn();
        System.out.println(gameState.getPlayerStateByIndex(0).getItemInEffect());

        gameState.beginTurn();

        System.out.println(gameState.getPlayerStateByIndex(0).getItemInEffect());


        System.out.println(testP1.getCurrHealth());

    }


    @Test
    /* Test multiple hits over a series of turns */
    public void multipleTurnsTest() {

    }


    @Test
    /* Test Item.PROCRUSTEAN_IRON */
    public void flatDamageTest() {
        testP1.setItemHolding(Item.PROCRUSTEAN_IRON);
        AttackAction attackOne = new AttackAction(1, 0);
        gameState.executeAttack(attackOne);
        assertEquals(testP1.getCurrHealth(), testP1.getEffectiveStatSet().getMaxHealth() - 4); // wizard damage

        AttackAction attackTwo = new AttackAction(2, 0);
        gameState.executeAttack(attackTwo);
        assertEquals(testP1.getCurrHealth(), testP1.getEffectiveStatSet().getMaxHealth() - 8);

    }

    @Test
    public void deathTest() {
        testP1.setItemHolding(Item.MAGIC_STAFF);
        AttackAction attackOne = new AttackAction(0, 1);
        gameState.executeAttack(attackOne);
        System.out.println(testP2.isDead());

        gameState.endTurn();
        System.out.println(testP2.isDead());
        gameState.beginTurn();

        System.out.println(testP2.isDead());

        System.out.println(testP2.getItemHolding());
        assertEquals(testP2.getItemHolding(), Item.NONE);

    }

}



