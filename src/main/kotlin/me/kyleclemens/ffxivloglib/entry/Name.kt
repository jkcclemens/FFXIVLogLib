package me.kyleclemens.ffxivloglib.entry

import me.kyleclemens.ffxivloglib.get
import me.kyleclemens.ffxivloglib.indexOf
import me.kyleclemens.ffxivloglib.toRealString
import me.kyleclemens.ffxivloglib.toUnsignedInt

data class Name(val realName: String, val displayName: String) {
    companion object {
        val MARKER = listOf(0x02, 0x27)

        fun parseName(rawName: String) = this.parseName(rawName.map { it.toUnsignedInt() })

        fun parseName(rawName: List<Int>): Name? {
            if (rawName.isEmpty()) return null
            if (rawName[null, 2] == Name.MARKER) {
                val realLength = rawName[2] + 2 // additional two because it's length from itself
                return Name(
                    rawName[9, realLength].toRealString(),
                    rawName[realLength + 1, rawName.indexOf(0x02, realLength)].toRealString()
                )
            }
            return rawName.toRealString().let { Name(it, it) }
        }
    }
}
