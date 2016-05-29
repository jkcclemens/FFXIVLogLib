package me.kyleclemens.ffxivloglib.message.parts

class IconPart(val bytes: List<Int>) : Part {

    companion object {
        val MARKER = listOf(0x02, 0x12)
    }

    override val displayText = "<icon ${bytes.map { Integer.toHexString(it) }.joinToString("")}>"
}
