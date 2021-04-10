package jvmusin.interpreter.token

/**
 * Reads a comma-separated list of tokens of type [T] using [tokenReader] to read tokens.
 */
@OptIn(ExperimentalStdlibApi::class)
fun <T : Token> readCommaSeparatedTokens(queue: SymbolQueue, tokenReader: TokenReader<out T>): List<T> {
    return buildList<T> {
        while (true) {
            val cur = queue.readSafely {
                if (isNotEmpty()) readSymbol(',')
                readToken(tokenReader)
            } ?: break
            add(cur)
        }
    }
}
