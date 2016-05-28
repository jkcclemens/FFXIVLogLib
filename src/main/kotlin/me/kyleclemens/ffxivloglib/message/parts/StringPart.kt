package me.kyleclemens.ffxivloglib.message.parts

class StringPart(val string: String) : Part {
    override val displayText: String = this.string

    override fun toString() = this.displayText
}
