package jvmusin.interpreter.token

import jvmusin.interpreter.element.NumberElement

/**
 * Constant expression token.
 *
 * Represents a constant integer value, possibly negative.
 *
 * @property value The integer value itself.
 */
data class ConstantExpressionToken(val value: Int) : ExpressionToken {
    override fun toElement() = NumberElement(value)
}

/**
 * Constant expression token reader.
 *
 * Allows to read [ConstantExpressionToken]-s.
 */
object ConstantExpressionTokenReader : TokenReader<ConstantExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = queue.readSafely {
        val minus = tryReadSymbol('-')
        val digits = readString { it.isDigit() }
        val number = digits.toIntOrNull() ?: throw SyntaxError()
        ConstantExpressionToken(if (minus != null) -number else number)
    }
}
