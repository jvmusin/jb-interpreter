package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class IdentifierToken(val name: String) : ExpressionToken {
    override val symbolsUsed = name.length
    override fun run(callEnvironment: CallEnvironment) = callEnvironment.getParameter(name)
}

class IdentifierTokenReader(private val characterTokenReader: CharacterTokenReader) : TokenReader<IdentifierToken> {
    override fun tryRead(queue: SymbolQueue): IdentifierToken? {
        val value = generateSequence { characterTokenReader.tryRead(queue) }.joinToString("")
        if (value.isEmpty()) return null
        return IdentifierToken(value)
    }
}
