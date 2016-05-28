package me.kyleclemens.ffxivloglib.message.parts

import me.kyleclemens.ffxivloglib.entry.Name

class NamePart(val name: Name) : Part {
    override val displayText: String = this.name.displayName

    override fun toString() = this.displayText
}
