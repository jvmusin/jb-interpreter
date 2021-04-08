package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class ProgramToken(val functions: FunctionDefinitionListToken, val body: ExpressionToken) : Token {
    override val symbolsUsed = functions.symbolsUsed + body.symbolsUsed
}

class ProgramTokenReader(
    private val functionDefinitionListTokenReader: FunctionDefinitionListTokenReader,
    private val generalExpressionTokenReader: GeneralExpressionTokenReader
) : TokenReader<ProgramToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        val functions = readToken(functionDefinitionListTokenReader)
        val body = readToken(generalExpressionTokenReader)
        ProgramToken(functions, body)
    }
}
