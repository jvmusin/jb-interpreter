package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class FunctionDefinitionToken(val name: String, val parameters: ParameterListToken, val body: ExpressionToken) : Token {
    override val symbolsUsed = name.length + 1 + parameters.symbolsUsed + 3 + body.symbolsUsed + 1
}

class FunctionDefinitionTokenReader(
    private val identifierTokenReader: IdentifierTokenReader,
    private val parameterListTokenReader: ParameterListTokenReader,
    private val expressionTokenReader: ExpressionTokenReader<*>
) : TokenReader<FunctionDefinitionToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        val identifier = readToken(identifierTokenReader)
        readString("(")
        val args = readToken(parameterListTokenReader)
        readString(")={")
        val body = readToken(expressionTokenReader)
        readString("}")
        FunctionDefinitionToken(identifier.name, args, body)
    }
}
