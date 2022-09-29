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

import static org.junit.jupiter.api.Assertions.*;


public class BuyActionTest {

    private PlayerState testP1;
    private PlayerState testP2;
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
        testP1.setItemHolding(Item.NONE);
        testP2.setItemHolding(Item.NONE);
    }

    @Test
    /* Test a player successfully buying an item  */
    public void buyItem() {

        Item anemoiWings = Item.ANEMOI_WINGS;

        testP1.setGold(100);

        // Create new BuyAction
        BuyAction buyAnemoiWings = new BuyAction(0, anemoiWings);
        gameState.executeBuy(buyAnemoiWings);

        // Test that the player has successfully acquired the item
        assertEquals(testP1.getItemHolding(), Item.ANEMOI_WINGS);
        // Test that the gold has decreased by the correct amount
        assertEquals(testP1.getGold(), 100 - Item.ANEMOI_WINGS.getCost());

    }

    @Test
    /* Test a player buying a new item */
    public void replaceItem() {
        buyItem();
        BuyAction buyStrengthPotion  = new BuyAction(0, Item.STRENGTH_POTION);
        gameState.executeBuy(buyStrengthPotion);

        assertEquals(testP1.getItemHolding(), Item.STRENGTH_POTION);
        assertNotEquals(testP1.getItemHolding(), Item.ANEMOI_WINGS);

        assertEquals(testP1.getGold(), 100 - Item.ANEMOI_WINGS.getCost() - Item.STRENGTH_POTION.getCost());
    }

    @Test
    /* Test two players buying the same item */
    public void buySameItem() {
        testP1.setGold(100);
        testP2.setGold(100);

        BuyAction player1Buy = new BuyAction(0, Item.ANEMOI_WINGS);
        BuyAction player2Buy = new BuyAction(1, Item.ANEMOI_WINGS);

        gameState.executeBuy(player1Buy);
        gameState.executeBuy(player2Buy);

        assertEquals(testP1.getItemHolding(), Item.ANEMOI_WINGS);
        assertEquals(testP2.getItemHolding(), Item.ANEMOI_WINGS);

    }

    @Test
    /* Test a player that can't afford an item */
    public void cantAffordItem() {
        testP1.setGold(Item.ANEMOI_WINGS.getCost()-1);

        BuyAction buyAction = new BuyAction(0, Item.ANEMOI_WINGS);

        assertNotEquals(testP1.getItemHolding(), Item.ANEMOI_WINGS);
        assertEquals(testP1.getGold(), Item.ANEMOI_WINGS.getCost()-1);
    }

    @Test
    /* Test a player in the wrong spot (not in spawn) */
    public void notInShop() {
        testP1.getPosition().translate(new Position(1, 1));
        testP1.setGold(100);

        BuyAction buyAction = new BuyAction(0, Item.ANEMOI_WINGS);

        assertNotEquals(testP1.getItemHolding(), Item.ANEMOI_WINGS);
        assertEquals(testP1.getGold(), 100);
    }
}
