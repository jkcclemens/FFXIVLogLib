package me.kyleclemens.ffxivloglib.message

import me.kyleclemens.ffxivloglib.entry.Name
import me.kyleclemens.ffxivloglib.get
import me.kyleclemens.ffxivloglib.message.parts.AutoTranslatePart
import me.kyleclemens.ffxivloglib.message.parts.IconPart
import me.kyleclemens.ffxivloglib.message.parts.NamePart
import me.kyleclemens.ffxivloglib.message.parts.Part
import me.kyleclemens.ffxivloglib.message.parts.StringPart
import me.kyleclemens.ffxivloglib.toUnsignedInt

class GameMessage(val raw: String) : Message {
    override val parts: List<Part> = this.parseMessageParts()
    override val displayMessage: String
        get() = this.parts.map { it.displayText }.joinToString("")

    private fun parseMessageParts(): List<Part> {
        val parts = mutableListOf<Part>()
        var skip = 0
        val plainMessage = StringBuilder()
        for ((i, c) in this.raw.withIndex()) {
            if (skip > 0) {
                skip--
                continue
            }
            if (c.toUnsignedInt() == Name.MARKER[0] && this.raw.length > i && this.raw[i + 1].toUnsignedInt() == Name.MARKER[1]) {
                if (this.raw[i + 3].toUnsignedInt() == 0x01) {
                    if (!plainMessage.isEmpty()) {
                        parts.add(StringPart(plainMessage.toString()))
                        plainMessage.setLength(0)
                    }
                    val beginningLength = this.raw[i + 2].toUnsignedInt()
                    val endMarker = this.raw.indexOf(0x02.toChar(), i + beginningLength)
                    if (endMarker != -1) {
                        val extraLength = this.raw[endMarker + 2].toUnsignedInt()
                        val name = Name.parseName(this.raw[i, endMarker + extraLength])!!
                        parts.add(NamePart(name))
                        skip = endMarker + extraLength - i + 2
                        continue
                    }
                }
            }
            if (c.toUnsignedInt() == AutoTranslatePart.MARKER[0] && this.raw.length > i && this.raw[i + 1].toUnsignedInt() == AutoTranslatePart.MARKER[1]) {
                if (!plainMessage.isEmpty()) {
                    parts.add(StringPart(plainMessage.toString()))
                    plainMessage.setLength(0)
                }
            }
            if (c.toUnsignedInt() == IconPart.MARKER[0] && this.raw.length > i && this.raw[i + 1].toUnsignedInt() == IconPart.MARKER[1]) {
                if (!plainMessage.isEmpty()) {
                    parts.add(StringPart(plainMessage.toString()))
                    plainMessage.setLength(0)
                }
                val length = this.raw[i + 2].toUnsignedInt()
                val endMarker = this.raw.indexOf(0x03.toChar(), i + length)
                parts.add(IconPart(this.raw[i + 3, endMarker].map { it.toUnsignedInt() }))
                skip = endMarker - i
                continue
            }
            plainMessage.append(c)
        }
        if (!plainMessage.isEmpty()) {
            parts.add(StringPart(plainMessage.toString()))
        }
        return parts
    }

    override fun toString() = this.displayMessage
}
