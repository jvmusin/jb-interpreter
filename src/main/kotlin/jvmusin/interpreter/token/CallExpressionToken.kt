package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.element.FunctionCallElement

/**
 * Call expression token.
 *
 * Used to call other functions.
 * Consists of a function name to invoke and argument list, where arguments are separated by a comma.
 * Arguments might be constants, identifiers (variables), or other expressions.
 * Looks like
 *
 * ```
 * functionName(argument1,argument2)
 * ```
 *
 * @property functionName Name of a function to call.
 * @property arguments Arguments to use when calling a function.
 */
data class CallExpressionToken(val functionName: String, val arguments: ArgumentListToken) : ExpressionToken {
    override val symbolsUsed = functionName.length + 1 + arguments.symbolsUsed + 1
    override fun toElement() = FunctionCallElement(functionName, arguments.values.map { it.toElement() })
}

/**
 * Call expression token reader.
 *
 * Allows to read [CallExpressionToken]-s.
 */
object CallExpressionTokenReader : ExpressionTokenReader<CallExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        val identifier = readToken(IdentifierTokenReader)
        readString("(")
        val args = readToken(ArgumentListTokenReader)
        readString(")")
        CallExpressionToken(identifier.name, args)
    }
}
