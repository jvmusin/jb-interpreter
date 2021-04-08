package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.element.VariableElement

data class IdentifierToken(val name: String) : ExpressionToken {
    override val symbolsUsed = name.length
    override fun toElement() = VariableElement(name)
}

object IdentifierTokenReader : TokenReader<IdentifierToken> {
    override fun tryRead(queue: SymbolQueue): IdentifierToken? {
        val value = generateSequence { queue.tryPoll { it == '_' || it.isLetter() } }.joinToString("")
        if (value.isEmpty()) return null
        return IdentifierToken(value)
    }
}
