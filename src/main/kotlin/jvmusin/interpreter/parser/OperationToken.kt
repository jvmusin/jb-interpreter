package jvmusin.interpreter.parser

/**
 * All known operations and their meaning.
 */
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

/**
 * Operation token.
 *
 * Represents a binary operation.
 *
 * @property value Character representation of the operation.
 */
data class OperationToken(val value: Char) : Token {
    /**
     * This operation's respective function.
     */
    val function get() = knownOperations[value]!!
}

/**
 * Operation token reader.
 *
 * Allows reading operations.
 *
 * All known operations are defined in [knownOperations].
 */
object OperationTokenReader : TokenReader<OperationToken> {
    override fun tryRead(queue: SymbolQueue) = queue.readSafely { OperationToken(readSymbol { it in knownOperations }) }
}
