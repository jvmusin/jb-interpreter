package jvmusin.interpreter.parser

/**
 * Argument list token.
 *
 * Represents list of arguments when calling a function.
 *
 * Looks like a comma-separated list of expressions.
 *
 * Used by [CallExpressionToken].
 *
 * @property values Expressions that will be evaluated and used as function arguments.
 */
data class ArgumentListToken(val values: List<ExpressionToken>) : Token

/**
 * Argument list token reader.
 *
 * Allows to read [ArgumentListToken]-s. Used by [CallExpressionTokenReader].
 */
object ArgumentListTokenReader : TokenReader<ArgumentListToken> {
    override fun tryRead(queue: SymbolQueue): ArgumentListToken? {
        return readCommaSeparatedTokens(queue, GeneralExpressionTokenReader).let {
            if (it.isEmpty()) null else ArgumentListToken(it)
        }
    }
}
