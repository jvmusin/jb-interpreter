package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class CallExpressionToken(val functionName: String, val arguments: ArgumentListToken) : ExpressionToken {
    override val symbolsUsed = functionName.length + 1 + arguments.symbolsUsed + 1
}

class CallExpressionTokenReader(
    private val identifierTokenReader: IdentifierTokenReader,
    private val argumentListTokenReader: ArgumentListTokenReader,
    override val subExpressionReader: ExpressionTokenReader<*>
) : ExpressionTokenReader<CallExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        val identifier = readToken(identifierTokenReader)
        readString("(")
        val args = readToken(argumentListTokenReader)
        readString(")")
        CallExpressionToken(identifier.name, args)
    }
}
