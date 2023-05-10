package com.example.starter

import com.fasterxml.jackson.databind.ObjectMapper

data class Message(
    var message: String = "",
    var title: String = ""
) {

    companion object {
        fun fromJson(jsonString: String): Message {
            // jackson
            val mapper = ObjectMapper()
            return mapper.readValue(jsonString, Message::class.java)
        }

        fun toJson(messageObj: Message): String {
            val mapper = ObjectMapper()
            val jsonResult = mapper.writeValueAsString(messageObj)

            return jsonResult
        }
    }
}
