package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class FunctionDefinitionListToken(val functions: List<FunctionDefinitionToken>) : Token {
    override val symbolsUsed = functions.sumOf { it.symbolsUsed } + functions.size
}

class FunctionDefinitionListTokenReader(
    private val functionDefinitionTokenReader: FunctionDefinitionTokenReader
) : TokenReader<FunctionDefinitionListToken> {
    override fun tryRead(queue: SymbolQueue): FunctionDefinitionListToken? {
        return readSeparatedTokens(queue, functionDefinitionTokenReader, '\n', true)?.let(::FunctionDefinitionListToken)
    }
}
