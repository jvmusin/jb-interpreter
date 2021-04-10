package jvmusin.interpreter.token

import jvmusin.interpreter.element.ProgramElement

/**
 * Program token.
 *
 * Represents the whole program as a single token. Contains all defined functions
 * and body of a method to be executed when the program is launched.
 *
 * Looks like a list of functions (possibly empty) and a body on the next line.
 *
 * @property functions All defined in the program functions.
 * @property body Body of a program to be executed when the program is launched.
 */
data class ProgramToken(val functions: FunctionDefinitionListToken, val body: ExpressionToken) : Token {
    fun toElement(): ProgramElement {
        val functionElements = functions.values.mapIndexed { index, func -> func.toElement(index + 1) }
        return ProgramElement(functionElements, body.toElement())
    }
}

/**
 * Program token reader.
 *
 * Allows to read [ProgramToken].
 *
 * Returns `null` if after reading a program something left in a queue.
 */
object ProgramTokenReader : TokenReader<ProgramToken> {
    override fun tryRead(queue: SymbolQueue) = queue.readSafely {
        val functions = readToken(FunctionDefinitionListTokenReader)
        val body = readToken(GeneralExpressionTokenReader)
        if (queue.isEmpty()) ProgramToken(functions, body) else null
    }
}
