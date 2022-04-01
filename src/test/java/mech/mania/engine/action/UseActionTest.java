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

public class UseActionTest {

    private PlayerState testP1;
    private PlayerState testP2;
    private GameState gameState;

    @BeforeEach
    public void setup() {
        // Configure a default gameState

        List<PlayerState> playerStateList = Arrays.asList(new PlayerState[4]);
        playerStateList.set(0, new PlayerState(CharacterClass.KNIGHT, new Position(0, 0)));
        playerStateList.set(1, new PlayerState(CharacterClass.ARCHER, new Position(BOARD_SIZE - 1, 0)));
        playerStateList.set(2, new PlayerState());
        playerStateList.set(3, new PlayerState());

        gameState = new GameState(playerStateList);

        testP1 = gameState.getPlayerStateByIndex(0);
        testP2 = gameState.getPlayerStateByIndex(1);
        testP1.setItem(Item.NULL_ITEM);
        testP2.setItem(Item.NULL_ITEM);

    }

    @Test
    /* Test a player using a consumable item */
    public void useConsumable() {
        testP1.setItem(Item.STRENGTH_POTION);
        UseAction useAction = new UseAction(0);
        gameState.executeUse(useAction);

        assertEquals(testP1.getEffectiveStatSet().getDamage(), 6 + 4); // Knight damage + strength potion damage

        // Turn has ended
        gameState.updateItems();

        assertEquals(testP1.getEffectiveStatSet().getDamage(), 6);
        assertEquals(testP1.getItem(), Item.NULL_ITEM);

    }

    @Test
    /* Use class change */
    public void useClassChange() {
        testP1.setItem(Item.STEEL_TIPPED_ARROW);
        UseAction useAction = new UseAction(0);
        gameState.executeUse(useAction);
        assertEquals(testP1.getCharacterClass(), CharacterClass.ARCHER);
    }
    @Test
    /* Test a player having a permanent item */
    public void usePermanent() {
        testP1.setItem(Item.ANEMOI_WINGS);

        assertEquals(testP1.getEffectiveStatSet().getSpeed(), 2); // Item hasn't set in yet from buying it last turn

        // Test that multiple turns ending doesn't do anything
        // Turn 1:
        gameState.updateItems();
        assertEquals(testP1.getItem(), Item.ANEMOI_WINGS);
        assertEquals(testP1.getEffectiveStatSet().getSpeed(), 4); // Knight speed + anemoi_wings buff

        // Turn 2:
        gameState.updateItems();
        assertEquals(testP1.getItem(), Item.ANEMOI_WINGS);
        assertEquals(testP1.getEffectiveStatSet().getSpeed(), 4);

        // Test that using it doesn't do anything
        UseAction useAction = new UseAction(0);
        gameState.executeUse(useAction);
        assertEquals(testP1.getItem(), Item.ANEMOI_WINGS);
        assertEquals(testP1.getEffectiveStatSet().getSpeed(), 4);

        // Turn 3:
        gameState.updateItems();
        assertEquals(testP1.getItem(), Item.ANEMOI_WINGS);
        assertEquals(testP1.getEffectiveStatSet().getSpeed(), 4);
    }

}