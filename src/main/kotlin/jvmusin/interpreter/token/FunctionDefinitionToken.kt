package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.element.FunctionElement

data class FunctionDefinitionToken(
    val name: String,
    val parameters: ParameterListToken,
    val body: ExpressionToken
) : Token {
    override val symbolsUsed = name.length + 1 + parameters.symbolsUsed + 3 + body.symbolsUsed + 1
    fun toElement(lineNumber: Int) = FunctionElement(lineNumber, name, parameters.values, body.toElement())
}

object FunctionDefinitionTokenReader : TokenReader<FunctionDefinitionToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        val identifier = readToken(IdentifierTokenReader)
        readString("(")
        val args = readToken(ParameterListTokenReader)
        readString(")={")
        val body = readToken(GeneralExpressionTokenReader)
        readString("}")
        FunctionDefinitionToken(identifier.name, args, body)
    }
}
