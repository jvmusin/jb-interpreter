package jvmusin.interpreter

import jvmusin.interpreter.token.Token

class SymbolQueue(private val source: String) {
    private var at: Int = 0

    private inline fun get(index: Int, checkResult: ((Char?) -> Boolean) = { true }): Char? {
        val res = source.getOrNull(index)
        if (!checkResult(res)) throw SyntaxError()
        return res
    }

    fun isFinished() = at !in source.indices

    fun tryPoll(checkResult: (Char) -> Boolean): Char? {
        return get(at)
            .let { c -> if (c != null && checkResult(c)) c else null }
            .also { c -> if (c != null) at++ }
    }

    fun poll(checkResult: (Char) -> Boolean) = tryPoll(checkResult) ?: throw SyntaxError()

    fun rollback(count: Int) = run { at -= count }
    fun rollback(token: Token) = rollback(token.symbolsUsed)
}
