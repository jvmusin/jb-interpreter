package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class ProgramToken(val functions: FunctionDefinitionListToken, val body: ExpressionToken) : Token {
    override val symbolsUsed = functions.symbolsUsed + body.symbolsUsed

    fun run(): Int {
        val funcs = Functions(functions.functions.associateBy { it.name })
        val params = Parameters(emptyMap())
        val callEnvironment = CallEnvironment(funcs, params)
        return body.run(callEnvironment)
    }
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
