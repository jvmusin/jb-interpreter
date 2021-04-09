package jvmusin.interpreter

import jvmusin.interpreter.token.Token

/**
 * Queue of symbols used to parse programs.
 *
 * It allows to read symbols if they are correct (checked by a predicate) and also to rollback read operations.
 *
 * @property source A string to read symbols from.
 */
class SymbolQueue(private val source: String) {

    /**
     * Caret pointing to index of a symbol that will be read next.
     */
    private var caret: Int = 0

    /**
     * Returns next symbol if the queue is not empty and [checkResult] returns `true`.
     * Otherwise, returns `null`.
     */
    private inline fun get(checkResult: ((Char?) -> Boolean) = { true }): Char? {
        val res = source.getOrNull(caret)
        if (!checkResult(res)) throw SyntaxError()
        return res
    }

    /**
     * Returns `true` if the queue is empty or `false` otherwise.
     */
    fun isEmpty() = caret == source.length

    /**
     * Returns next symbol if the queue is not empty and [checkResult] returns `true` on the next symbol.
     * Otherwise, returns `null`.
     *
     * If returns not `null`, then moves the [caret] one index forward.
     */
    fun tryPoll(checkResult: (Char) -> Boolean): Char? {
        return get()
            .let { c -> if (c != null && checkResult(c)) c else null }
            .also { c -> if (c != null) caret++ }
    }

    /**
     * Returns next symbol if the queue is not empty and [checkResult] returns `true` on the next symbol.
     * Otherwise, throws [SyntaxError].
     */
    fun poll(checkResult: (Char) -> Boolean) = tryPoll(checkResult) ?: throw SyntaxError()

    /**
     * Moves [caret] back by [count] indices.
     */
    fun rollback(count: Int) {
        if (count < 0 || caret - count !in 0..source.length) throw IllegalArgumentException("Can't move caret by $count left")
        caret -= count
    }

    /**
     * Moves [caret] back by [token.symbolsUsed][Token.symbolsUsed] indices.
     */
    fun rollback(token: Token) = rollback(token.symbolsUsed)
}
