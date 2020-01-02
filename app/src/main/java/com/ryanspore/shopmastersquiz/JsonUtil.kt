package com.ryanspore.shopmastersquiz

import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class JsonUtil {
    fun loadJSONFromStream(stream: InputStream): String? {
        var json: String? = null
        json = try {
            val size: Int = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

}