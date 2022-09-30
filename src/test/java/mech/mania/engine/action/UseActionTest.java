package mech.mania.engine.action;

import mech.mania.engine.*;
import java.util.Arrays;
import java.util.List;


import mech.mania.engine.player.CharacterClass;
import mech.mania.engine.player.Item;
import mech.mania.engine.player.PlayerState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class UseActionTest {

    private PlayerState testP1;
    private PlayerState testP2;
    private GameState gameState;

    @BeforeEach
    public void setup() {
        // Configure a default gameState

        List<CharacterClass> classes = Arrays.asList(new CharacterClass[4]);
        classes.set(0, CharacterClass.KNIGHT);
        classes.set(1, CharacterClass.ARCHER);
        classes.set(2, CharacterClass.KNIGHT);
        classes.set(3, CharacterClass.KNIGHT);



        gameState = new GameState(classes);

        testP1 = gameState.getPlayerStateByIndex(0);
        testP2 = gameState.getPlayerStateByIndex(1);
        testP1.setItemHolding(Item.NONE);
        testP2.setItemHolding(Item.NONE);

    }

    @Test
    /* Test a player using a consumable item */
    public void useConsumable() {
        testP1.setItemHolding(Item.STRENGTH_POTION);
        UseAction useAction = new UseAction(0, true);
        gameState.executeUse(useAction);

        assertEquals(testP1.getEffectiveStatSet().getDamage(), 6 + 4); // Knight damage + strength potion damage

        // Turn has ended
        gameState.endTurn();
        gameState.beginTurn();

        assertEquals(testP1.getEffectiveStatSet().getDamage(), 6);
        assertEquals(testP1.getItemHolding(), Item.NONE);

    }
    @Test
    public void useConsumableFalse() {
        testP1.setItemHolding(Item.STRENGTH_POTION);
        UseAction useAction = new UseAction(0, false);
        gameState.executeUse(useAction);

        assertEquals(testP1.getEffectiveStatSet().getDamage(), 6); // Knight damage + strength potion damage


    }

    @Test
    /* Use class change */
    public void useClassChange() {
        testP1.setItemHolding(Item.STEEL_TIPPED_ARROW);
        UseAction useAction = new UseAction(0, true);
        gameState.executeUse(useAction);
        assertEquals(testP1.getCharacterClass(), CharacterClass.ARCHER);
    }

    @Test
    public void useClassChangeFalse() {
        testP1.setItemHolding(Item.STEEL_TIPPED_ARROW);
        UseAction useAction = new UseAction(0, false);
        gameState.executeUse(useAction);
        assertEquals(testP1.getCharacterClass(), CharacterClass.KNIGHT);
    }
    @Test
    /* Test a player having a permanent item */
    public void usePermanent() {
        testP1.setItemHolding(Item.ANEMOI_WINGS);

        assertEquals(testP1.getEffectiveStatSet().getSpeed(), 2); // Item hasn't set in yet from buying it last turn

        // Test that multiple turns ending doesn't do anything
        // Turn 1:
        gameState.updateItemsAtBeginTurn();
        assertEquals(testP1.getItemHolding(), Item.ANEMOI_WINGS);
        assertEquals(testP1.getEffectiveStatSet().getSpeed(), 4); // Knight speed + anemoi_wings buff

        // Turn 2:
        gameState.updateItemsAtBeginTurn();
        assertEquals(testP1.getItemHolding(), Item.ANEMOI_WINGS);
        assertEquals(testP1.getEffectiveStatSet().getSpeed(), 4);

        // Test that using it doesn't do anything
        UseAction useAction = new UseAction(0, false);
        gameState.executeUse(useAction);
        assertEquals(testP1.getItemHolding(), Item.ANEMOI_WINGS);
        assertEquals(testP1.getEffectiveStatSet().getSpeed(), 4);

        // Turn 3:
        gameState.updateItemsAtBeginTurn();
        assertEquals(testP1.getItemHolding(), Item.ANEMOI_WINGS);
        assertEquals(testP1.getEffectiveStatSet().getSpeed(), 4);
    }

}
