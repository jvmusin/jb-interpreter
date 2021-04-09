package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

/**
 * Safe token reader.
 *
 * Used to read tokens from [SymbolQueue] and if some call fails, then the queue returns to the state it was before the call.
 *
 * Heavily relies on the right [Token.symbolsUsed]
 */
interface SafeTokenReader {
    /**
     * Reads a string from a queue and if it's not equal to [s], then stops the whole reading.
     */
    fun readString(s: String)

    /**
     * Reads a token of type [T] using [reader] and if it fails (returns `null`), then stops the whole reading.
     */
    fun <T : Token> readToken(reader: TokenReader<out T>): T
}

fun <T> readTokenSafely(queue: SymbolQueue, block: SafeTokenReader.() -> T): T? {
    var tokensRead = 0

    val reader = object : SafeTokenReader {
        override fun readString(s: String) = s.forEach { c -> queue.poll { it == c }.also { tokensRead++ } }
        override fun <T : Token> readToken(reader: TokenReader<out T>) =
            reader.read(queue).also { tokensRead += it.symbolsUsed }
    }

    return try {
        reader.block()
    } catch (e: Throwable) {
        queue.rollback(tokensRead)
        null
    }
}
