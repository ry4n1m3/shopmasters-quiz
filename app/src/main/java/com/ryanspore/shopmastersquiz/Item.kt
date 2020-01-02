package com.ryanspore.shopmastersquiz

data class Item(
    val img: String,
    val dname: String,
    val qual: String,
    val cost: Int,
    val desc: String,
    val attrib: String,
    val components: List<String>?,
    val created: Boolean
) {

    companion object {
        val baseUrl = "https://cdn.dota2.com/apps/dota2/images/items/"
    }

    fun imgUrl(): String {
        return baseUrl + img
    }
}