package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class IdentifierToken(val name: String) : ExpressionToken {
    override val symbolsUsed = name.length
}

class IdentifierTokenReader : TokenReader<IdentifierToken> {
    override fun tryRead(queue: SymbolQueue): IdentifierToken? {
        val value = generateSequence { queue.tryPoll { it == '_' || it.isLetter() } }.joinToString("")
        if (value.isEmpty()) return null
        return IdentifierToken(value)
    }
}
