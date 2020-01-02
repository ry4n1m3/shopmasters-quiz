package com.ryanspore.shopmastersquiz

class Quiz(val shop: Shop) {
    private val items: List<String> = shop.upgradeItems.shuffled().toMutableList()
    private val currentIndex = 0

    fun currentQuestion(): Question {
        return Question(shop, items[currentIndex], potentialComponents(items[currentIndex]))
    }

    private fun potentialComponents(itemKey: String): List<String> {
        val realComponents = shop.items[itemKey]?.components ?: emptyList()

        val diversionComponents = shop.componentItems.shuffled().subList(0, 8-realComponents.size)

        return realComponents.plus(diversionComponents)
    }
}