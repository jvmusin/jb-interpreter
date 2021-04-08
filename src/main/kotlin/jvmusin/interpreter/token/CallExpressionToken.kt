package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.element.FunctionCallElement

data class CallExpressionToken(val functionName: String, val arguments: ArgumentListToken) : ExpressionToken {
    override val symbolsUsed = functionName.length + 1 + arguments.symbolsUsed + 1
    override fun toElement() = FunctionCallElement(functionName, arguments.values.map { it.toElement() })
}

object CallExpressionTokenReader : ExpressionTokenReader<CallExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        val identifier = readToken(IdentifierTokenReader)
        readString("(")
        val args = readToken(ArgumentListTokenReader)
        readString(")")
        CallExpressionToken(identifier.name, args)
    }
}
