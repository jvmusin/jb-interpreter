package jvmusin.interpreter.element

data class FunctionElement(
    val lineNumber: Int,
    val name: String,
    val parameterNames: List<String>,
    val body: Element
): Element {
    override fun invoke(environment: CallEnvironment): Int {
        try {
             return body(environment)
        } catch (e: Throwable) {
            throw IllegalArgumentException("FIX IT")
        }
    }
}
