package jvmusin.interpreter.element

data class BinaryExpressionElement(
    val left: Element,
    val right: Element,
    val operation: (Int, Int) -> Int
) : Element {
    override fun invoke(environment: CallEnvironment) = operation(left(environment), right(environment))
}
