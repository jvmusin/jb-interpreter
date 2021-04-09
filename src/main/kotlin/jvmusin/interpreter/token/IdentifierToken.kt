package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.element.VariableElement

/**
 * Identifier token.
 *
 * Represents a string, consisting of lower and upper case letter and an underscore.
 *
 * @property name Name of the identifier.
 */
data class IdentifierToken(val name: String) : ExpressionToken {
    override val symbolsUsed = name.length
    override fun toElement() = VariableElement(name)
}

/**
 * Identifier token reader
 *
 * Allows to read [IdentifierToken]-s.
 */
object IdentifierTokenReader : ExpressionTokenReader<IdentifierToken> {
    override fun tryRead(queue: SymbolQueue): IdentifierToken? {
        val value = generateSequence { queue.tryPoll { it == '_' || it.isLetter() } }.joinToString("")
        if (value.isEmpty()) return null
        return IdentifierToken(value)
    }
}
