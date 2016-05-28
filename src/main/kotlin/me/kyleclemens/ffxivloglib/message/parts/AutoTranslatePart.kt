package me.kyleclemens.ffxivloglib.message.parts

class AutoTranslatePart(val raw: String) : Part {

    companion object {
        val MARKER = listOf(0x02, 0x2e)
    }

    override val displayText: String = "<AUTOTRANSLATE NOT YET SUPPORTED>"
}
