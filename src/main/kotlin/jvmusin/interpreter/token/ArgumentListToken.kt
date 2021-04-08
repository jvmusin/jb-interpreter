package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class ArgumentListToken(val args: List<ExpressionToken>) : Token {
    override val symbolsUsed = args.sumOf(ExpressionToken::symbolsUsed) + args.size - 1
}

class ArgumentListTokenReader(
    private val generalExpressionTokenReader: GeneralExpressionTokenReader
) : TokenReader<ArgumentListToken> {
    override fun tryRead(queue: SymbolQueue): ArgumentListToken? {
        return readSeparatedTokens(queue, generalExpressionTokenReader)?.let { ArgumentListToken(it) }
    }
}
