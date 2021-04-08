package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

class GeneralExpressionTokenReader(
    private val readers: List<ExpressionTokenReader<*>>
) : ExpressionTokenReader<ExpressionToken> {
    override val subExpressionReader: ExpressionTokenReader<*> = this
    override fun tryRead(queue: SymbolQueue) = readers.asSequence().mapNotNull { it.read(queue) }.firstOrNull()
}
