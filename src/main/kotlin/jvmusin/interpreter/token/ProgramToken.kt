package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.SyntaxError
import jvmusin.interpreter.element.ProgramElement

data class ProgramToken(val functions: FunctionDefinitionListToken, val body: ExpressionToken) : Token {
    override val symbolsUsed = functions.symbolsUsed + body.symbolsUsed

    fun toElement(): ProgramElement {
        val functionElements = functions.values.mapIndexed { index, func -> func.toElement(index + 1) }
        return ProgramElement(functionElements, body.toElement())
    }
}

object ProgramTokenReader : TokenReader<ProgramToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        val functions = readToken(FunctionDefinitionListTokenReader)
        val body = readToken(GeneralExpressionTokenReader)
        if (!queue.isFinished()) throw SyntaxError()
        ProgramToken(functions, body)
    }
}
