package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.element.NumberElement

/**
 * Constant expression token.
 *
 * Represents a constant integer value, possibly negative.
 *
 * @property value The integer value itself.
 */
data class ConstantExpressionToken(val value: Int) : ExpressionToken {
    override val symbolsUsed = value.toString().length
    override fun toElement() = NumberElement(value)
}

/**
 * Constant expression token reader.
 *
 * Allows to read [ConstantExpressionToken]-s.
 */
object ConstantExpressionTokenReader : TokenReader<ConstantExpressionToken> {
    override fun tryRead(queue: SymbolQueue): ConstantExpressionToken? {
        val minus = queue.tryPoll { it == '-' }
        val digits = generateSequence { queue.tryPoll { it.isDigit() } }.toList()
        val number = digits.joinToString("").toIntOrNull()
        if (number == null) {
            if (minus != null) queue.rollback(1)
            queue.rollback(digits.size)
            return null
        }
        return ConstantExpressionToken(if (minus != null) -number else number)
    }
}
