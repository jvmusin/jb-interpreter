package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class ConstantExpressionToken(val value: Int) : ExpressionToken {
    override val symbolsUsed = value.toString().length
}

class ConstantExpressionTokenReader(
    private val numberTokenReader: NumberTokenReader
) : TokenReader<ConstantExpressionToken> {
    override fun tryRead(queue: SymbolQueue): ConstantExpressionToken? {
        val minus = queue.tryPoll { it == '-' }
        val number = numberTokenReader.tryRead(queue)
        if (number == null) {
            if (minus != null) queue.rollback(1)
            return null
        }
        val sign = if (minus != null) -1 else 1
        return ConstantExpressionToken(number.value * sign)
    }
}
