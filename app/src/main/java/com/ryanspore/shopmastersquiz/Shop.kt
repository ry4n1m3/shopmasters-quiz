package com.ryanspore.shopmastersquiz

class Shop(val items: Map<String, Item>) {
    private val componentsLookup: Map<String, List<String>>
    val baseItems: Set<String>
    val componentItems: Set<String>
    val upgradeItems: Set<String>

    init {
        componentsLookup = items.mapValues { entry ->
            entry.value.components ?: emptyList()
        }
        val tempComponentItems = mutableSetOf<String>()
        val tempUpgradeItems = mutableSetOf<String>()
        val tempBaseItems = mutableSetOf<String>()
        items.keys.forEach { itemKey ->
            val components: List<String> = items[itemKey]?.components ?: emptyList()
            components.forEach { componentKey -> tempComponentItems.add(componentKey) }
            if (components.isNotEmpty()) {
                tempUpgradeItems.add(itemKey)
            } else {
                tempBaseItems.add(itemKey)
            }
        }
        baseItems = tempBaseItems.toSet()
        componentItems = tempComponentItems.toSet()
        upgradeItems = tempUpgradeItems.toSet()
    }

    fun hasRecipe(itemKey: String): Boolean {
        return recipeCost(itemKey) > 0
    }

    fun recipeCost(itemKey: String): Int {
        if (componentsLookup[itemKey]?.isEmpty()!!) {
            return 0
        }
        val item = items[itemKey]
        val componentCostSum = componentsLookup[itemKey]?.map { componentKey ->
            items[componentKey]?.cost
        }?.sumBy { cost -> cost ?: 0 } ?: 0
        return (item?.cost ?: 0) - componentCostSum
    }

    fun componentCheck(upgradeKey: String, componentKeys: List<String>): Boolean {
        val keys = if (upgradeKey == "power_treads") treadsSwap(componentKeys) else componentKeys

        val allowableComponentCombinations = expandList(upgradeKey)
        return allowableComponentCombinations.any { possibleComponentCombination ->
            isMatchingCombination(possibleComponentCombination, keys)
        }
    }

    private fun isMatchingCombination(allowableComponentCombination: List<String>, submittedComponentCombination: List<String>): Boolean {
        val allowableNonRecipes = allowableComponentCombination.filter { component -> !component.startsWith("recipe") }
        val submittedNonRecipes = submittedComponentCombination.filter { component -> component != "recipe" }
        return submittedNonRecipes.containsAll(allowableNonRecipes)
                && allowableComponentCombination.size == submittedComponentCombination.size
    }

    private fun expandList(upgradeKey: String): List<List<String>> {
        val desiredComponents = items[upgradeKey]?.components ?: emptyList()
        val result = mutableListOf(fullComponents(upgradeKey))
//        val stack = desiredComponents.toMutableList()
//        while (stack.size > 0) {
//            val item = stack.removeAt(0)
//            if (items[item]?.components?.isNotEmpty() == true) {
//                result.add(desiredComponents.minus(item).plus(fullComponents(item)))
//            }
//        }
        return result
    }

    private fun fullComponents(itemKey: String): List<String> {
        val itemComponents = items[itemKey]?.components ?: emptyList()
        if (itemComponents.any { component-> component.contains("recipe") }) return itemComponents
        return if (hasRecipe(itemKey)) itemComponents.plus("recipe_$itemKey") else itemComponents
    }

    private fun treadsSwap(componentKeys: List<String>): List<String> {
        if (componentKeys.contains("robe"))
            return componentKeys.minus("robe").plus("belt_of_strength")
        if (componentKeys.contains("boots_of_elves"))
            return componentKeys.minus("boots_of_elves").plus("belt_of_strength")
        return componentKeys
    }
}