package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

/**
 * List of all known [ExpressionTokenReader]-s.
 */
private val readers = listOf(
    BinaryExpressionTokenReader,
    CallExpressionTokenReader,
    IfExpressionTokenReader,
    ConstantExpressionTokenReader,
    IdentifierTokenReader
)

/**
 * General expression token reader.
 *
 * Allows to read [ExpressionToken]-s when the exact type of a token is unknown.
 *
 * Tries to apply readers from [readers] one-by-one until non-null token is read.
 *
 * If none of the [readers] produced a token, then returns `null`
 */
object GeneralExpressionTokenReader : ExpressionTokenReader<ExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = readers.asSequence().mapNotNull { it.tryRead(queue) }.firstOrNull()
}
