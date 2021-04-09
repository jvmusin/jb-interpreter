package jvmusin.interpreter.element

data class IfExpressionElement(
    val condition: Element,
    val ifTrue: Element,
    val ifFalse: Element
) : Element {
    override fun invokeUnsafely(environment: CallEnvironment): Int  {
        return if (condition(environment) != 0) {
            ifTrue(environment)
        } else {
            ifFalse(environment)
        }
    }

    override fun validate(environment: CallEnvironment) {
        condition.validate(environment)
        ifTrue.validate(environment)
        ifFalse.validate(environment)
    }

    override fun toString(): String {
        return "[$condition]?($ifTrue):($ifFalse)"
    }
}
