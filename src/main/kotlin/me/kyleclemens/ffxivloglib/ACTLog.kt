package me.kyleclemens.ffxivloglib

import me.kyleclemens.ffxivloglib.entry.FFXIVLogEntry
import me.kyleclemens.ffxivloglib.entry.actlog.ACTLogEntryParser
import java.io.File
import java.io.FileInputStream
import java.util.Collections

class ACTLog(val file: File, val live: Boolean = false) : Log {

    private val _entries = Collections.synchronizedList(mutableListOf<FFXIVLogEntry>())
    override val entries: List<FFXIVLogEntry>
        get() {
            val ssEntries = this._entries.toList()
            if (this.live) {
                synchronized(this.lock) {
                    this.lastAccessedSize = ssEntries.size
                }
            }
            return Collections.unmodifiableList(ssEntries)
        }
    private var thread: ACTLogUpdaterThread? = null
    private var lastAccessedSize: Int = -1
    val newEntries: List<FFXIVLogEntry>
        get() {
            if (!this.live) {
                throw IllegalStateException("ACTLog was not initialized as a live log, but a live method was invoked.")
            }
            synchronized(this.lock) {
                val ssLastAccessedSize = this.lastAccessedSize
                val ssEntries = this._entries.toList()
                if (ssLastAccessedSize < ssEntries.size) {
                    this.lastAccessedSize = ssEntries.size
                    return ssEntries.subList(Math.max(0, ssLastAccessedSize), ssEntries.size)
                }
                return emptyList()
            }
        }
    private var lock: Any = Object()

    init {
        if (this.live) {
            this.startThread()
        } else {
            this._entries.addAll(this.file.readLines().filter { it.split("|")[0] == "00" }.map { ACTLogEntryParser(it).entry })
        }
    }

    fun stopThread() {
        if (!this.live) {
            throw IllegalStateException("ACTLog was not initialized as a live log, but a live method was invoked.")
        }
        val thread = this.thread ?: throw IllegalStateException("ACTLog thread is not started, so it cannot be stopped.")
        thread.stop = true
        this.thread = null
    }

    fun startThread() {
        if (!this.live) {
            throw IllegalStateException("ACTLog was not initialized as a live log, but a live method was invoked.")
        }
        if (this.thread != null) {
            throw IllegalStateException("ACTLog thread is already started; a new one cannot be started.")
        }
        this.thread = ACTLogUpdaterThread(this).apply { this.start() }
    }

    class ACTLogUpdaterThread(val log: ACTLog) : Thread() {

        var stop: Boolean = false
        val reader = FileInputStream(this.log.file).bufferedReader()

        override fun run() {
            while (true) {
                if (this.stop) break
                val line = this.reader.readLine()
                if (line == null) {
                    Thread.sleep(1000L)
                    continue
                }
                if (line.split("|")[0] != "00") continue
                this.log._entries.add(ACTLogEntryParser(line).entry)
            }
        }
    }

}

