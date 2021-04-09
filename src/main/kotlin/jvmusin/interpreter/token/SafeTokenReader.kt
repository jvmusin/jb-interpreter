package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

interface SafeTokenReader {
    fun readString(s: String)
    fun <T : Token> readToken(reader: TokenReader<out T>): T
}

fun <T> readTokenSafely(queue: SymbolQueue, block: SafeTokenReader.() -> T): T? {
    var tokensRead = 0

    class SafeTokenReaderImpl : SafeTokenReader {
        override fun readString(s: String) = s.forEach { c -> queue.poll { it == c }.also { tokensRead++ } }
        override fun <T : Token> readToken(reader: TokenReader<out T>) =
            reader.read(queue).also { tokensRead += it.symbolsUsed }
    }

    return try {
        SafeTokenReaderImpl().block()
    } catch (e: Throwable) {
        queue.rollback(tokensRead)
        null
    }
}
