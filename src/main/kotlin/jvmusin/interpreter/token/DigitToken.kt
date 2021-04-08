package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue

data class DigitToken(val digit: Int) : Token {
    override val symbolsUsed = 1
}

@OptIn(ExperimentalStdlibApi::class)
class DigitTokenReader : TokenReader<DigitToken> {
    override fun tryRead(queue: SymbolQueue): DigitToken? {
        return queue.tryPoll { it.isDigit() }?.digitToInt()?.let(::DigitToken)
    }
}
