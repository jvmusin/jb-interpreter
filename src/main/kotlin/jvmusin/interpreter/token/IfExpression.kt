package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class IfExpression(
    val condition: ExpressionToken,
    val ifTrue: ExpressionToken,
    val ifFalse: ExpressionToken
) : ExpressionToken {
    override val symbolsUsed = 1 + condition.symbolsUsed + 3 + ifTrue.symbolsUsed + 3 + ifFalse.symbolsUsed + 1

    override fun run(callEnvironment: CallEnvironment): Int {
        return if (condition.run(callEnvironment) == 1) {
            ifTrue.run(callEnvironment)
        } else {
            ifFalse.run(callEnvironment)
        }
    }
}

class IfExpressionReader(
    override val subExpressionReader: ExpressionTokenReader<*>
) : ExpressionTokenReader<IfExpression> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        readString("[")
        val condition = readToken(subExpressionReader)
        readString("]?(")
        val ifTrue = readToken(subExpressionReader)
        readString("):(")
        val ifFalse = readToken(subExpressionReader)
        readString(")")
        IfExpression(condition, ifTrue, ifFalse)
    }
}
