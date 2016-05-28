package me.kyleclemens.ffxivloglib

import me.kyleclemens.ffxivloglib.entry.FFXIVLogEntry
import me.kyleclemens.ffxivloglib.entry.ffxivlog.FFXIVLogEntryParser
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Collections

class FFXIVLog(private val file: File) : Log, DataInputStream(FileInputStream(file)) {

    private val _entries = mutableListOf<FFXIVLogEntry>()
    override val entries: List<FFXIVLogEntry>
        get() {
            return Collections.unmodifiableList(this._entries)
        }

    init {
        val bb = ByteBuffer.wrap(this.readBytes()).order(ByteOrder.LITTLE_ENDIAN)
        val headerStart = bb.int
        val headerEnd = bb.int
        val headerLength = headerEnd - headerStart
        val header = (0..headerLength - 1).map { bb.int }
        val headerEndOffset = headerLength * 4 + 8
        for (headerPos in 0..header.size - 1) {
            val startPos: Int
            val endPos: Int
            if (headerPos == 0) {
                startPos = headerEndOffset
                endPos = startPos + header[headerPos]
            } else {
                startPos = headerEndOffset + header[headerPos - 1]
                endPos = startPos + header[headerPos] - header[headerPos - 1]
            }
            val size = endPos - startPos
            val entryBytes = (0..size - 1).map { bb.get().toInt() and 0xFF }
            this._entries.add(FFXIVLogEntryParser(entryBytes).entry)
        }
    }

}
