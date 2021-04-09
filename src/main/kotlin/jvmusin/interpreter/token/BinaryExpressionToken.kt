package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.element.BinaryExpressionElement
import jvmusin.interpreter.element.Operation

/**
 * Binary expression token.
 *
 * Looks like
 *
 * ```
 * (<expression><operation><expression>)
 * ```
 *
 * @property left First operand.
 * @property right Second operand.
 * @property operation Binary function to apply to operands.
 */
data class BinaryExpressionToken(
    val left: ExpressionToken,
    val right: ExpressionToken,
    val operation: OperationToken
) : ExpressionToken {
    override val symbolsUsed = 1 + left.symbolsUsed + 1 + right.symbolsUsed + 1
    override fun toElement(): BinaryExpressionElement {
        return BinaryExpressionElement(
            left.toElement(),
            right.toElement(),
            Operation(operation.value, operation.function)
        )
    }
}

/**
 * Binary expression token reader.
 *
 * Reads [BinaryExpressionToken]-s.
 */
object BinaryExpressionTokenReader : ExpressionTokenReader<BinaryExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        readString("(")
        val left = readToken(GeneralExpressionTokenReader)
        val operation = readToken(OperationTokenReader)
        val right = readToken(GeneralExpressionTokenReader)
        readString(")")
        BinaryExpressionToken(left, right, operation)
    }
}
