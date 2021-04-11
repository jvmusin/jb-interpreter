package jvmusin.interpreter.runner

/**
 * Binary expression element.
 *
 * Contains [left] and [right] operands as well as an [operation] to invoke on them.
 */
data class BinaryExpressionElement(
    val left: Element,
    val right: Element,
    val operation: BinaryOperation
) : Element {

    /**
     * Invokes [operation] on [left] and [right] operands using [environment] to evaluate operands values if needed
     * and returns the result of an [operation].
     */
    override fun invokeUnsafely(environment: CallEnvironment): Int {
        return operation(left(environment), right(environment))
    }

    /**
     * Recursively validates [left] and [right] operands with the given [environment]
     * and throws [ValidationError] if some validation errors occur.
     */
    override fun validate(environment: CallEnvironment) {
        left.validate(environment)
        right.validate(environment)
    }

    /**
     * Returns a string representation of operand.
     *
     * It has a form of `"(<left><operation><right>)"`.
     */
    override fun toString(): String {
        return "($left${operation.representation}$right)"
    }
}
