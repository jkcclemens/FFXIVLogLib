package me.kyleclemens.ffxivloglib.entry

import me.kyleclemens.ffxivloglib.message.Message
import java.util.Date

data class FFXIVLogEntry(
    val type: FFXIVEntryType,
    val timestamp: Date,
    val sender: Name?,
    val message: Message
)
