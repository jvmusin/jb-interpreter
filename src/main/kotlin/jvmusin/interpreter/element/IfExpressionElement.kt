package jvmusin.interpreter.element

/**
 * If expression element.
 *
 * Represents ternary if. If [condition] gives not zero, then returns [ifTrue], otherwise [ifFalse].
 */
data class IfExpressionElement(
    val condition: Element,
    val ifTrue: Element,
    val ifFalse: Element
) : Element {

    /**
     * Invokes [condition] with [environment] and if it returns not zero,
     * then invokes [ifTrue] and returns it's result, otherwise does the same with [ifFalse].
     */
    override fun invokeUnsafely(environment: CallEnvironment): Int {
        return if (condition(environment) != 0) {
            ifTrue(environment)
        } else {
            ifFalse(environment)
        }
    }

    /**
     * Validates the if using [environment].
     *
     * Validates [condition], [ifTrue] and [ifFalse] and throws [ValidationError] in case of validation failure.
     */
    override fun validate(environment: CallEnvironment) {
        condition.validate(environment)
        ifTrue.validate(environment)
        ifFalse.validate(environment)
    }

    /**
     * Returns string representation of `this` if. Looks like `"[condition]?(ifTrue):(ifFalse)"`.
     */
    override fun toString(): String {
        return "[$condition]?($ifTrue):($ifFalse)"
    }
}
