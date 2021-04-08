package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.element.IfExpressionElement

data class IfExpressionToken(
    val condition: ExpressionToken,
    val ifTrue: ExpressionToken,
    val ifFalse: ExpressionToken
) : ExpressionToken {
    override val symbolsUsed = 1 + condition.symbolsUsed + 3 + ifTrue.symbolsUsed + 3 + ifFalse.symbolsUsed + 1
    override fun toElement() = IfExpressionElement(condition.toElement(), ifTrue.toElement(), ifFalse.toElement())
}

object IfExpressionTokenReader: ExpressionTokenReader<IfExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        readString("[")
        val condition = readToken(GeneralExpressionTokenReader)
        readString("]?(")
        val ifTrue = readToken(GeneralExpressionTokenReader)
        readString("):(")
        val ifFalse = readToken(GeneralExpressionTokenReader)
        readString(")")
        IfExpressionToken(condition, ifTrue, ifFalse)
    }
}
