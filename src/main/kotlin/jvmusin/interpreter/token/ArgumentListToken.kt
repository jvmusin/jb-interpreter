package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class ArgumentListToken(val values: List<ExpressionToken>) : Token {
    override val symbolsUsed = values.sumOf(ExpressionToken::symbolsUsed) + values.size - 1
}

object ArgumentListTokenReader : TokenReader<ArgumentListToken> {
    override fun tryRead(queue: SymbolQueue): ArgumentListToken? {
        return readSeparatedTokens(queue, GeneralExpressionTokenReader)?.let { ArgumentListToken(it) }
    }
}
