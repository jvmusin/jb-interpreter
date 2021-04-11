package jvmusin.interpreter.parser

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
    override fun toElement() = IfExpressionElement(condition.toElement(), ifTrue.toElement(), ifFalse.toElement())
}

/**
 * If expression token reader.
 *
 * Allows to read [IfExpressionToken]-s.
 */
object IfExpressionTokenReader : ExpressionTokenReader<IfExpressionToken> {
    override fun tryRead(queue: SymbolQueue) = queue.readSafely {
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
