package jvmusin.interpreter.token

/**
 * Parameter list token.
 *
 * Represents list of function parameters.
 *
 * Looks like a comma-separated list of identifiers.
 *
 * Used by [FunctionDefinitionToken].
 *
 * @property values Names of function parameters.
 */
data class ParameterListToken(val values: List<String>) : Token

/**
 * Parameter list token reader.
 *
 * Allows to read [ParameterListToken]-s. Used by [FunctionDefinitionTokenReader].
 */
object ParameterListTokenReader : TokenReader<ParameterListToken> {
    override fun tryRead(queue: SymbolQueue): ParameterListToken? {
        return readCommaSeparatedTokens(queue, IdentifierTokenReader).let {
            if (it.isEmpty()) null else ParameterListToken(it.map(IdentifierToken::name))
        }
    }
}
