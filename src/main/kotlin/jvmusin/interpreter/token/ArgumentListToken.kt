package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

/**
 * Argument list token.
 *
 * Represent list of arguments when calling a function.
 *
 * Looks like a comma-separated list of expressions.
 *
 * @property values Expressions that will be evaluated and used as function arguments.
 */
data class ArgumentListToken(val values: List<ExpressionToken>) : Token {
    override val symbolsUsed = values.sumOf { it.symbolsUsed } + values.size - 1
}

/**
 * Argument list token reader.
 *
 * Allows to read [ArgumentListToken]-s.
 */
object ArgumentListTokenReader : TokenReader<ArgumentListToken> {
    override fun tryRead(queue: SymbolQueue): ArgumentListToken? {
        return readSeparatedTokens(queue, GeneralExpressionTokenReader)?.let { ArgumentListToken(it) }
    }
}
