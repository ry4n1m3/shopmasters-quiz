package com.ryanspore.shopmastersquiz

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val json = JsonUtil().loadJSONFromStream(assets.open("itemdata.json"))
        val mapper = jacksonObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val itemLookup = json?.let { mapper.readValue<Map<String, Map<String, Item>>>(it)["itemdata"] }
        val blink = itemLookup?.get("blink")
        if(itemLookup != null) {
            findViewById<TextView>(R.id.item_name).setText(blink?.dname)
            Glide
                .with(this)
                .load(blink?.imgUrl())
                .into(findViewById(R.id.item_img))
        }
    }
}
