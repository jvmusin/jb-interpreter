package jvmusin.interpreter.token

import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.element.IfExpressionElement

/**
 * If expression token.
 *
 * Represents ternary-if expressions. Looks like
 * ```
 * [condition]?(ifTrue):(ifFalse)
 * ```
 *
 * If [condition] gives not zero, executes [ifTrue] block, otherwise executes [ifFalse] block.
 *
 * @property condition Condition to check.
 * @property ifTrue Expresion to evaluate if [condition] gives not zero.
 * @property ifFalse Expression to evaluate if [condition] gives zero.
 */
data class IfExpressionToken(
    val condition: ExpressionToken,
    val ifTrue: ExpressionToken,
    val ifFalse: ExpressionToken
) : ExpressionToken {
    override val symbolsUsed = 1 + condition.symbolsUsed + 3 + ifTrue.symbolsUsed + 3 + ifFalse.symbolsUsed + 1
    override fun toElement() = IfExpressionElement(condition.toElement(), ifTrue.toElement(), ifFalse.toElement())
}

/**
 * If expression token reader.
 *
 * Allows to read [IfExpressionToken]-s.
 */
object IfExpressionTokenReader : ExpressionTokenReader<IfExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = readTokenSafely(queue) {
        readString("[")
        val condition = readToken(GeneralExpressionTokenReader)
        readString("]?(")
        val ifTrue = readToken(GeneralExpressionTokenReader)
        readString("):(")
        val ifFalse = readToken(GeneralExpressionTokenReader)
        readString(")")
        IfExpressionToken(condition, ifTrue, ifFalse)
    }
}
