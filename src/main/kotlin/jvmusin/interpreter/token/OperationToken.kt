package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

private val knownOperations = mapOf<Char, (Int, Int) -> Int>(
    '+' to Int::plus,
    '-' to Int::minus,
    '*' to Int::times,
    '/' to Int::div,
    '%' to Int::rem,
    '>' to { a, b -> if (a > b) 1 else 0 },
    '<' to { a, b -> if (a < b) 1 else 0 },
    '=' to { a, b -> if (a == b) 1 else 0 }
)

data class OperationToken(val value: Char) : Token {
    override val symbolsUsed = 1

    private val function get() = knownOperations[value]!!
    fun apply(left: Int, right: Int) = function(left, right)
}

class OperationTokenReader : TokenReader<OperationToken> {
    override fun tryRead(queue: SymbolQueue): OperationToken? {
        return queue.tryPoll { it in knownOperations }?.let(::OperationToken)
    }
}
