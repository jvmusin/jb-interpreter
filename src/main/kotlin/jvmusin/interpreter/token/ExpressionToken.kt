package jvmusin.interpreter.token

import jvmusin.interpreter.element.Element

interface ExpressionToken : Token {
    fun toElement(): Element
}