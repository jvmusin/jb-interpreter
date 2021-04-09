package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

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
