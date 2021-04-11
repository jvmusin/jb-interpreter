package jvmusin.interpreter.parser

/**
 * Safe queue reader.
 *
 * Used to read data from [queue] and if reading fails, then the [queue] returns to the state it was before the call.
 */
class SafeQueueReader(private val queue: SymbolQueue) {
    /**
     * Reads a symbol matching [predicate] and if it was not read, then stops the whole reading.
     */
    fun readSymbol(predicate: (Char) -> Boolean) = queue.poll(predicate)

    /**
     * Reads a single symbol [expect] and if it was not read, then stops the whole reading.
     */
    fun readSymbol(expect: Char) = readSymbol { it == expect }

    /**
     * Tries to read symbol matching [predicate] and if it was not read, then returns `null`.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun tryReadSymbol(predicate: (Char) -> Boolean) = queue.tryPoll(predicate)

    /**
     * Tries to read symbol [expect] and if it was not read, then returns `null`.
     */
    fun tryReadSymbol(expect: Char) = tryReadSymbol { it == expect }

    /**
     * Reads a string from a queue and if it's not equal to [expect], then stops the whole reading.
     */
    fun readString(expect: String) = expect.map(this::readSymbol).joinToString("")

    /**
     * Reads a string where each symbol matches [predicate]. Stops reading when [predicate] returns `false`.
     */
    fun readString(predicate: (Char) -> Boolean) = generateSequence { tryReadSymbol(predicate) }.joinToString("")

    /**
     * Reads a token of type [T] using [reader] and if it fails (returns `null`), then stops the whole reading.
     */
    fun <T : Token> readToken(reader: TokenReader<T>) = reader.read(queue)
}

/**
 * Reads data from the queue by running [block] and returning it's result.
 *
 * If [block] throws an exception, then `this` queue returns to the state it was before calling [readSafely]
 * and `null` is returned.
 */
inline fun <T> SymbolQueue.readSafely(block: SafeQueueReader.() -> T): T? {
    val startCaret = caret

    return try {
        SafeQueueReader(this).block()
    } catch (e: Throwable) {
        caret = startCaret
        null
    }
}
