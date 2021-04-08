package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class ParameterListToken(val args: List<String>) : Token {
    override val symbolsUsed = args.sumOf { it.length } + args.size - 1
}

class ParameterListTokenReader(
    private val identifierTokenReader: IdentifierTokenReader
) : TokenReader<ParameterListToken> {
    override fun tryRead(queue: SymbolQueue): ParameterListToken? {
        return readSeparatedTokens(queue, identifierTokenReader)?.let {
            ParameterListToken(it.map(IdentifierToken::name))
        }
    }
}
