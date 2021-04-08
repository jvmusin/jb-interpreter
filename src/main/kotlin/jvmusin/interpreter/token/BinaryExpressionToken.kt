package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.element.BinaryExpressionElement

data class BinaryExpressionToken(
    val left: ExpressionToken,
    val right: ExpressionToken,
    val operation: OperationToken
) : ExpressionToken {
    override val symbolsUsed = 1 + left.symbolsUsed + 1 + right.symbolsUsed + 1
    override fun toElement() = BinaryExpressionElement(left.toElement(), right.toElement(), operation::apply)
}

object BinaryExpressionTokenReader: ExpressionTokenReader<BinaryExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        readString("(")
        val left = readToken(GeneralExpressionTokenReader)
        val operation = readToken(OperationTokenReader)
        val right = readToken(GeneralExpressionTokenReader)
        readString(")")
        BinaryExpressionToken(left, right, operation)
    }
}
