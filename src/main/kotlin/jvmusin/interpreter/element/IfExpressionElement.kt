package jvmusin.interpreter.element

data class IfExpressionElement(
    val condition: Element,
    val ifTrue: Element,
    val ifFalse: Element
) : Element {
    override fun invoke(environment: CallEnvironment): Int {
        return if (condition(environment) == 1) {
            ifTrue(environment)
        } else {
            ifFalse(environment)
        }
    }
}
