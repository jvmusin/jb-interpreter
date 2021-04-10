package jvmusin.interpreter.token

/**
 * Function definition list token.
 *
 * Represents a list of [FunctionDefinitionToken] functions, separated by `newline` (`\n`) symbols.
 *
 * @property values Functions in this list.
 */
data class FunctionDefinitionListToken(val values: List<FunctionDefinitionToken>) : Token

/**
 * Function definition list token reader.
 *
 * Allows to read [FunctionDefinitionListToken].
 */
object FunctionDefinitionListTokenReader : TokenReader<FunctionDefinitionListToken> {
    override fun tryRead(queue: SymbolQueue): FunctionDefinitionListToken {
        val functions = generateSequence {
            queue.readSafely { readToken(FunctionDefinitionTokenReader).also { readSymbol('\n') } }
        }
        return FunctionDefinitionListToken(functions.toList())
    }
}
