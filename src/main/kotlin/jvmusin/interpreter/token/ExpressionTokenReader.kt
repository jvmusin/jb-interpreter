package jvmusin.interpreter.token

interface ExpressionTokenReader<T : ExpressionToken> : TokenReader<T> {
    val subExpressionReader: ExpressionTokenReader<*>
}
