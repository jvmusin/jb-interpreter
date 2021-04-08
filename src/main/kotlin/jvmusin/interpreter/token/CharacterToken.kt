package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class CharacterToken(val value: Char) : Token {
    override val symbolsUsed: Int = 1
}

class CharacterTokenReader : TokenReader<CharacterToken> {
    override fun tryRead(queue: SymbolQueue): CharacterToken? {
        return queue.tryPoll { (it == '_' || it.isLetter()) }?.let { CharacterToken(it) }
    }
}