package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

/**
 * Function definition list token.
 *
 * Represents a list of [FunctionDefinitionToken] functions, separated by `newline` (`\n`) symbols.
 *
 * @property values Functions in this list.
 */
data class FunctionDefinitionListToken(val values: List<FunctionDefinitionToken>) : Token {
    override val symbolsUsed = values.sumOf { it.symbolsUsed } + values.size
}

/**
 * Function definition list token reader.
 *
 * Allows to read [FunctionDefinitionListToken].
 */
object FunctionDefinitionListTokenReader : TokenReader<FunctionDefinitionListToken> {
    override fun tryRead(queue: SymbolQueue): FunctionDefinitionListToken? {
        return readSeparatedTokens(
            queue = queue,
            tokenReader = FunctionDefinitionTokenReader,
            separator = '\n',
            endWithSeparator = true,
            allowEmptyResult = true
        )?.let(::FunctionDefinitionListToken)
    }
}
