package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

private val readers = listOf(
    BinaryExpressionTokenReader,
    CallExpressionTokenReader,
    IfExpressionTokenReader,
    ConstantExpressionTokenReader,
    IdentifierTokenReader
)

object GeneralExpressionTokenReader : ExpressionTokenReader<ExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = readers.asSequence().mapNotNull { it.tryRead(queue) }.firstOrNull()
}
