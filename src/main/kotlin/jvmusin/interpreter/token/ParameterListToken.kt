package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

/**
 * Parameter list token.
 *
 * Represents list of function parameters.
 *
 * Used by [FunctionDefinitionToken].
 *
 * @property values names of function parameters.
 */
data class ParameterListToken(val values: List<String>) : Token {
    override val symbolsUsed = values.sumOf { it.length } + values.size - 1
}

/**
 * Parameter list token reader.
 *
 * Allows to read [ParameterListToken]-s. Used by [FunctionDefinitionTokenReader].
 */
object ParameterListTokenReader : TokenReader<ParameterListToken> {
    override fun tryRead(queue: SymbolQueue): ParameterListToken? {
        return readSeparatedTokens(queue, IdentifierTokenReader)?.let {
            ParameterListToken(it.map(IdentifierToken::name))
        }
    }
}
