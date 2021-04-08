package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class ConstantExpressionToken(val value: Int, override val symbolsUsed: Int): ExpressionToken {
    override fun run(callEnvironment: CallEnvironment) = value
}

class ConstantExpressionTokenReader(private val numberTokenReader: NumberTokenReader) : TokenReader<ConstantExpressionToken> {
    override fun tryRead(queue: SymbolQueue): ConstantExpressionToken? {
        val minuses = generateSequence { queue.tryPoll { it == '-' } }.count()
        val number = numberTokenReader.tryRead(queue)
        if (number == null) {
            queue.rollback(minuses)
            return null
        }
        val sign = if (minuses % 2 == 0) 1 else -1
        return ConstantExpressionToken(number.value * sign, minuses + number.symbolsUsed)
    }
}
