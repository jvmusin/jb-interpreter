package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class CallExpressionToken(val identifier: String, val args: ArgumentListToken) : ExpressionToken {
    override val symbolsUsed = identifier.length + 1 + args.symbolsUsed + 1
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
