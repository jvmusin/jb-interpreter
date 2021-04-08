package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class NumberToken(val value: Int) : Token {
    override val symbolsUsed = value.toString().length
}

class NumberTokenReader(private val digitTokenReader: DigitTokenReader) : TokenReader<NumberToken> {
    override fun tryRead(queue: SymbolQueue): NumberToken? {
        val asString = generateSequence { digitTokenReader.tryRead(queue) }.joinToString("")
        if (asString.isEmpty()) return null
        val asInt = asString.toIntOrNull()
        if (asInt == null) {
            queue.rollback(asString.length)
            return null
        }
        return NumberToken(asInt)
    }
}
