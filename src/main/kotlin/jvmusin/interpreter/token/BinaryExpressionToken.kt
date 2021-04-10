package jvmusin.interpreter.token

import jvmusin.interpreter.element.BinaryExpressionElement
import jvmusin.interpreter.element.Operation

/**
 * Binary expression token. Looks like
 *
 * ```
 * (<left_expression><operation_sign><right_expression>)
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
    override fun tryRead(queue: SymbolQueue) = queue.readSafely {
        readString("(")
        val left = readToken(GeneralExpressionTokenReader)
        val operation = readToken(OperationTokenReader)
        val right = readToken(GeneralExpressionTokenReader)
        readString(")")
        BinaryExpressionToken(left, right, operation)
    }
}
