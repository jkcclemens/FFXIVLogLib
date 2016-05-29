package me.kyleclemens.ffxivloglib.message.parts

class IconPart(val bytes: Pair<Int, Int>) : Part {

    companion object {
        val MARKER = listOf(0x02, 0x12)
    }

    override val displayText = "<icon>"
}
