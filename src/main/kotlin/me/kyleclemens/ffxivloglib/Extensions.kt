package me.kyleclemens.ffxivloglib

fun List<Int>.toRealString() = this.map { it.toChar() }.joinToString("")

fun <T> List<T>.indexOf(any: T, from: Int) = this[from, this.size].indexOf(any) + from

fun String.indexOf(any: Char, from: Int) = this[from, this.length].indexOf(any) + from

fun Char.toUnsignedInt() = this.toInt() and 0xFF

/**
 * Python-esque slicing.
 */
operator fun <T> List<T>.get(start: Int?, end: Int?, emptyList: Boolean = false): List<T> {
    var realEnd = end ?: this.size
    var realStart = start ?: 0
    if (realEnd < 0) {
        realEnd += this.size
    }
    if (realStart < 0) {
        realStart += this.size
    }
    return try {
        this.subList(realStart, realEnd)
    } catch (ex: IllegalArgumentException) {
        if (emptyList) listOf() else throw ex
    }
}

/**
 * Python-esque slicing.
 */
operator fun String.get(start: Int?, end: Int?, emptyString: Boolean = false): String {
    var realEnd = end ?: this.length
    var realStart = start ?: 0
    if (realEnd < 0) {
        realEnd += this.length
    }
    if (realStart < 0) {
        realStart += this.length
    }
    return try {
        this.substring(realStart, realEnd)
    } catch (ex: IllegalArgumentException) {
        if (emptyString) "" else throw ex
    }
}
