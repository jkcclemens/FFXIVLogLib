package me.kyleclemens.ffxivloglib.entry.actlog

import me.kyleclemens.ffxivloglib.entry.FFXIVEntryType
import me.kyleclemens.ffxivloglib.entry.FFXIVLogEntry
import me.kyleclemens.ffxivloglib.entry.LogEntryParser
import me.kyleclemens.ffxivloglib.entry.Name
import me.kyleclemens.ffxivloglib.get
import me.kyleclemens.ffxivloglib.message.GameMessage
import java.text.SimpleDateFormat

class ACTLogEntryParser(private val raw: String) : LogEntryParser {

    companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX")
    }

    override val entry: FFXIVLogEntry
        get() = this.parse()

    private fun parse(): FFXIVLogEntry {
        val parts = this.raw.split('|')
        val timestamp = ACTLogEntryParser.dateFormat.parse(parts[1])
        val type = FFXIVEntryType.fromCode(Integer.parseInt(parts[2][2, null], 16))
        val sender = Name.parseName(parts[3])
        val message = parts.subList(4, parts.size).joinToString("|")
        return FFXIVLogEntry(
            type = type,
            timestamp = timestamp,
            sender = sender,
            message = GameMessage(message)
        )
    }
}
