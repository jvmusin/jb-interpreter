package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class ParameterListToken(val values: List<String>) : Token {
    override val symbolsUsed = values.sumOf { it.length } + values.size - 1
}

object ParameterListTokenReader : TokenReader<ParameterListToken> {
    override fun tryRead(queue: SymbolQueue): ParameterListToken? {
        return readSeparatedTokens(queue, IdentifierTokenReader)?.let {
            ParameterListToken(it.map(IdentifierToken::name))
        }
    }
}
