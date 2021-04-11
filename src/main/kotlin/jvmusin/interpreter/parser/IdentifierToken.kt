package jvmusin.interpreter.parser

import jvmusin.interpreter.element.VariableElement

/**
 * Identifier token.
 *
 * Represents a string, consisting of lower and upper case letter and an underscore.
 *
 * @property name Name of the identifier.
 */
data class IdentifierToken(val name: String) : ExpressionToken {
    override fun toElement() = VariableElement(name)
}

/**
 * Identifier token reader.
 *
 * Reads [IdentifierToken]-s.
 *
 * If name of the identifier is empty, returns `null`.
 */
object IdentifierTokenReader : ExpressionTokenReader<IdentifierToken> {
    override fun tryRead(queue: SymbolQueue): IdentifierToken? = queue.readSafely {
        readString { it == '_' || it.isLetter() }.ifEmpty { null }?.let(::IdentifierToken)
    }
}
