package jvmusin.interpreter.parser

import jvmusin.interpreter.runner.Element

/**
 * Expression token.
 *
 * Represents a token that can be evaluated.
 */
interface ExpressionToken : Token {

    /**
     * Transforms this token to [Element].
     */
    fun toElement(): Element
}
