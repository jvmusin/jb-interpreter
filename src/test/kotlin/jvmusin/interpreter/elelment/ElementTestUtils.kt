package jvmusin.interpreter.elelment

import jvmusin.interpreter.element.*

/**
 * Returns an environment with a single mocked function f(x)=x and a variable x=1
 */
fun getTestEnvironment(): CallEnvironment {
    val f = FunctionElement(1, "f", listOf("x"), VariableElement("x"))
    return CallEnvironment(buildFunctions(listOf(f)), Variables(mapOf("x" to 1)))
}
