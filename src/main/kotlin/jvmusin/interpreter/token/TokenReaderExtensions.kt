package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

/**
 * Reads a list of separated by [separator] tokens of type [T] from [queue] using [tokenReader] to read tokens.
 *
 * Tokens may end with a separator or not, depending on [endWithSeparator] value.
 *
 * Empty result will be evaluated to `null` if [allowEmptyResult] is `false`.
 */
fun <T : Token> readSeparatedTokens(
    queue: SymbolQueue,
    tokenReader: TokenReader<out T>,
    separator: Char = ',',
    endWithSeparator: Boolean = false,
    allowEmptyResult: Boolean = false
): List<T>? {
    val tokens = mutableListOf<T>()
    while (true) {
        if (tokens.isNotEmpty() && queue.tryPoll { it == separator } == null) break
        val token = tokenReader.tryRead(queue)
        if (token == null) {
            if (tokens.isNotEmpty()) queue.rollback(1)
            break
        }
        tokens += token
    }
    if (tokens.isNotEmpty() && endWithSeparator && queue.tryPoll { it == separator } == null) {
        queue.rollback(tokens.removeLast())
    }
    if (!allowEmptyResult && tokens.isEmpty()) return null
    return tokens
}
