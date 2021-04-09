package jvmusin.interpreter.element

data class BinaryExpressionElement(
    val left: Element,
    val right: Element,
    val operation: Operation
) : Element {
    override fun invoke(environment: CallEnvironment) = operation(left(environment), right(environment))
    override fun validate(environment: CallEnvironment) {
        left.validate(environment)
        right.validate(environment)
    }

    override fun toString(): String {
        return "($left${operation.representation}$right)"
    }
}
