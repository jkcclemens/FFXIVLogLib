package me.kyleclemens.ffxivloglib

import me.kyleclemens.ffxivloglib.entry.FFXIVLogEntry

interface Log {

    val entries: List<FFXIVLogEntry>

}
