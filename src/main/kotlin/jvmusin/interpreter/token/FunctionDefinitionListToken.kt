package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class FunctionDefinitionListToken(val values: List<FunctionDefinitionToken>) : Token {
    override val symbolsUsed = values.sumOf { it.symbolsUsed } + values.size
}

object FunctionDefinitionListTokenReader : TokenReader<FunctionDefinitionListToken> {
    override fun tryRead(queue: SymbolQueue): FunctionDefinitionListToken? {
        return readSeparatedTokens(queue, FunctionDefinitionTokenReader, '\n', true)?.let(::FunctionDefinitionListToken)
    }
}
