package me.kyleclemens.ffxivloglib.entry.ffxivlog

import me.kyleclemens.ffxivloglib.entry.FFXIVEntryType
import me.kyleclemens.ffxivloglib.entry.FFXIVLogEntry
import me.kyleclemens.ffxivloglib.entry.LogEntryParser
import me.kyleclemens.ffxivloglib.entry.Name
import me.kyleclemens.ffxivloglib.get
import me.kyleclemens.ffxivloglib.message.GameMessage
import java.nio.ByteBuffer
import java.util.Date

class FFXIVLogEntryParser(private val raw: List<Int>) : LogEntryParser {

    private companion object {
        private val colon = ':'.toInt()
    }

    override val entry: FFXIVLogEntry
        get() = this.parse()

    private data class RawEntryParts(val header: List<Int>, val sender: List<Int>, val message: List<Int>)

    private fun getParts(): RawEntryParts {
        // Search for first colon after skipping six bytes, since false positives can be found in the header
        val header = this.raw[6, null].indexOf(colon).let { this.raw[0, it + 6] }
        val sender = this.raw[header.size + 1, this.raw.size].let { it[0, it.indexOf(colon)] }
        val message = this.raw[header.size + sender.size + 2, this.raw.size]
        return RawEntryParts(header.toList(), sender.toList(), message.toList())
    }

    private fun parse(): FFXIVLogEntry {
        val parts = this.getParts()
        val timestamp = Date(ByteBuffer.wrap(parts.header.toList()[null, 4].reversed().map { it.toByte() }.toByteArray()).int * 1000L)
        val sender = Name.parseName(parts.sender)
        val message = parts.message.map { it.toChar() }.joinToString("")
        return FFXIVLogEntry(
            type = FFXIVEntryType.fromCode(parts.header[4]),
            timestamp = timestamp,
            sender = sender,
            message = GameMessage(message)
        )
    }

}
