package me.kyleclemens.ffxivloglib.message

import me.kyleclemens.ffxivloglib.message.parts.Part

interface Message {

    val parts: List<Part>
    val displayMessage: String

}
