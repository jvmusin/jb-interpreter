package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class NumberToken(val value: Int) : Token {
    override val symbolsUsed = value.toString().length
}

object NumberTokenReader : TokenReader<NumberToken> {
    override fun tryRead(queue: SymbolQueue): NumberToken? {
        val asString = generateSequence { queue.tryPoll { it.isDigit() } }.joinToString("")
        if (asString.isEmpty()) return null
        val asInt = asString.toIntOrNull()
        if (asInt == null) {
            queue.rollback(asString.length)
            return null
        }
        return NumberToken(asInt)
    }
}
