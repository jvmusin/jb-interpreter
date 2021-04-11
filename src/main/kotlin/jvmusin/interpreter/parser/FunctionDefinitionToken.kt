package jvmusin.interpreter.parser

import jvmusin.interpreter.runner.FunctionElement

/**
 * Function definition token.
 *
 * Represents a function. Looks like
 *
 * ```
 * functionName(arg1,arg2,arg3)={expression}
 * ```
 *
 * Empty argument list is prohibited.
 *
 * @property name Name of the function.
 * @property parameters Parameters of the function.
 * @property body Function body.
 */
data class FunctionDefinitionToken(
    val name: String,
    val parameters: ParameterListToken,
    val body: ExpressionToken
) : Token {
    fun toElement(lineNumber: Int) = FunctionElement(lineNumber, name, parameters.values, body.toElement())
}

/**
 * Function definition token reader.
 *
 * Allows to read [FunctionDefinitionToken]-s. Uses [ParameterListTokenReader] to read function parameters.
 */
object FunctionDefinitionTokenReader : TokenReader<FunctionDefinitionToken> {
    override fun tryRead(queue: SymbolQueue) = queue.readSafely {
        val identifier = readToken(IdentifierTokenReader)
        readString("(")
        val args = readToken(ParameterListTokenReader)
        readString(")={")
        val body = readToken(GeneralExpressionTokenReader)
        readString("}")
        FunctionDefinitionToken(identifier.name, args, body)
    }
}
