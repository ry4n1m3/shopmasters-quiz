package com.ryanspore.shopmastersquiz

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.FileInputStream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ShopTest {
    private val items: Map<String, Item>
    private val shop: Shop

    init {
        val json = JsonUtil().loadJSONFromStream(FileInputStream("src/main/assets/itemdata.json"))
        val mapper = jacksonObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        items = json?.let { mapper.readValue<Map<String, Map<String, Item>>>(it)["itemdata"] } ?: emptyMap()
        shop = Shop(items)
    }

    @Test
    fun hasRecipe() {
        assertFalse(shop.hasRecipe("blink"))
        assertFalse(shop.hasRecipe("monkey_king_bar"))
        assertTrue(shop.hasRecipe("black_king_bar"))
    }

    @Test
    fun componentItems() {
        assertTrue(shop.componentItems.contains("blink"))
        assertFalse(shop.componentItems.contains("monkey_king_bar"))
        assertFalse(shop.componentItems.contains("clarity"))
    }

    @Test
    fun upgradeItems() {
        assertFalse(shop.upgradeItems.contains("blink"))
        assertTrue(shop.upgradeItems.contains("monkey_king_bar"))
        assertFalse(shop.upgradeItems.contains("clarity"))
    }

    @Test
    fun recipeCost() {
        assertEquals(300, shop.recipeCost("buckler"))
        assertEquals(1450, shop.recipeCost("black_king_bar"))
    }

    @Test
    fun componentCheck() {
        assertTrue(shop.componentCheck("bloodstone", listOf("kaya", "soul_booster")))
        assertFalse(shop.componentCheck("bloodstone", listOf("pers", "soul_booster")))
        assertFalse(shop.componentCheck("black_king_bar", listOf("ogre_axe", "mithril_hammer")))
        assertTrue(shop.componentCheck("black_king_bar", listOf("ogre_axe", "mithril_hammer", "recipe")))
    }

    @Test @Disabled("To be implemented")
    fun componentCheck_deepChecking() {
        assertTrue(shop.componentCheck("bloodstone", listOf("kaya", "point_booster", "vitality_booster", "energy_booster")))
        assertTrue(shop.componentCheck("bloodstone", listOf("staff_of_wizardry", "robe", "recipe", "soul_booster")))
        assertTrue(shop.componentCheck("bloodstone", listOf("staff_of_wizardry", "robe", "recipe", "point_booster", "vitality_booster", "energy_booster")))
    }

    @Test
    fun componentCheck_treads() {
        assertTrue(shop.componentCheck("power_treads", listOf("boots", "gloves", "belt_of_strength")))
        assertTrue(shop.componentCheck("power_treads", listOf("boots", "gloves", "boots_of_elves")))
        assertTrue(shop.componentCheck("power_treads", listOf("boots", "gloves", "robe")))
        assertFalse(shop.componentCheck("power_treads", listOf("boots", "gloves", "robe", "robe")))
    }

    @Test
    fun componentCheck_travels() {
        assertTrue(shop.componentCheck("travel_boots", listOf("boots", "recipe")))
        assertTrue(shop.componentCheck("travel_boots_2", listOf("travel_boots", "recipe")))
    }
}