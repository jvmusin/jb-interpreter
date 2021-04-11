package jvmusin.interpreter.parser

/**
 * Expression token reader.
 *
 * Reads [ExpressionToken]-s of type [T].
 */
interface ExpressionTokenReader<T : ExpressionToken> : TokenReader<T>
