package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.SyntaxError

/**
 * Token reader.
 *
 * Allows to read tokens from [SymbolQueue].
 *
 * @param T type of tokens created by the reader.
 */
interface TokenReader<T : Token> {

    /** Reads token from [queue] or throws [SyntaxError] in case of reading failure. */
    fun read(queue: SymbolQueue): T = tryRead(queue) ?: throw SyntaxError()

    /**
     * Either reads token from [queue] and returns `non null` result,
     * or returns `null` and resets [queue] to its initial state.*/
    fun tryRead(queue: SymbolQueue): T?
}
