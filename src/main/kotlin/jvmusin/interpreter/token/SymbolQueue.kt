package jvmusin.interpreter.token

/**
 * Queue of symbols used to parse programs.
 *
 * It allows to read symbols if they are correct (checked by some predicate)
 * and also to move caret to arbitrary positions for repeatable reading.
 *
 * @property source A string to read symbols from.
 */
class SymbolQueue(private val source: String) {

    /**
     * Caret pointing to index of a symbol that will be read next.
     */
    var caret: Int = 0
        set(value) {
            if (value !in 0..source.length) throw IllegalArgumentException("Caret our of possible range")
            field = value
        }

    /**
     * Returns `true` if the queue is empty or `false` otherwise.
     */
    fun isEmpty() = caret == source.length

    /**
     * Returns next symbol if the queue is not empty and [checkResult] returns `true` on the next symbol.
     * Otherwise, returns `null`.
     *
     * Moves [caret] if symbol was read and returned successfully.
     */
    fun tryPoll(checkResult: (Char) -> Boolean): Char? {
        return source.getOrNull(caret)
            .let { c -> if (c != null && checkResult(c)) c else null }
            .also { c -> if (c != null) caret++ }
    }

    /**
     * Returns next symbol if the queue is not empty and [checkResult] returns `true` on the next symbol.
     * Otherwise, throws [SyntaxError].
     *
     * Moves [caret] if symbol was read and returned successfully.
     */
    fun poll(checkResult: (Char) -> Boolean) = tryPoll(checkResult) ?: throw SyntaxError()
}
