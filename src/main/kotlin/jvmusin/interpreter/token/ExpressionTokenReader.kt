package jvmusin.interpreter.token

/**
 * Expression token reader.
 *
 * Reads [ExpressionToken]-s of type [T].
 */
interface ExpressionTokenReader<T : ExpressionToken> : TokenReader<T>
