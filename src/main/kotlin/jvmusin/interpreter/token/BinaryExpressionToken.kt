package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class BinaryExpressionToken(
    val left: ExpressionToken,
    val right: ExpressionToken,
    val operation: OperationToken
) : ExpressionToken {
    override val symbolsUsed = 1 + left.symbolsUsed + 1 + right.symbolsUsed + 1
}

class BinaryExpressionTokenReader(
    private val operationTokenReader: OperationTokenReader,
    override val subExpressionReader: ExpressionTokenReader<*>
) : ExpressionTokenReader<BinaryExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        readString("(")
        val left = readToken(subExpressionReader)
        val operation = readToken(operationTokenReader)
        val right = readToken(subExpressionReader)
        readString(")")
        BinaryExpressionToken(left, right, operation)
    }
}
