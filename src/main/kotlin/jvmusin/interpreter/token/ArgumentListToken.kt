package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class ArgumentListToken(val values: List<ExpressionToken>) : Token {
    override val symbolsUsed = values.sumOf(ExpressionToken::symbolsUsed) + values.size - 1
}

class ArgumentListTokenReader(
    private val generalExpressionTokenReader: GeneralExpressionTokenReader
) : TokenReader<ArgumentListToken> {
    override fun tryRead(queue: SymbolQueue): ArgumentListToken? {
        return readSeparatedTokens(queue, generalExpressionTokenReader)?.let { ArgumentListToken(it) }
    }
}
