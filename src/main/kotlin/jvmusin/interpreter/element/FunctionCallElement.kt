package jvmusin.interpreter.element

/**
 * Function call element.
 *
 * Represents a function [functionName] call with the given [arguments].
 */
data class FunctionCallElement(
    val functionName: String,
    val arguments: List<Element>
) : Element {

    /**
     * Invokes `this` function with the given [environment] and returns invocation result.
     *
     * Firstly, evaluates all the function parameters
     * and then calls the function with [CallEnvironment.variables], defined for this function, and returns its result.
     */
    override fun invokeUnsafely(environment: CallEnvironment): Int {
        val function = environment.getFunction(functionName, arguments.size)
        val argumentValues = arguments.map { it.invoke(environment) }
        val parameterNames = function.parameterNames
        return function(environment.copy(variables = Variables(parameterNames.zip(argumentValues).toMap())))
    }

    /**
     * Validates all the [arguments] using [environment].
     *
     * If there is no function with the given [functionName], then [FunctionNotFoundError] is thrown.
     *
     * If there is a function with the given [functionName], but not with the given number of [arguments],
     * then [ArgumentNumberMismatchError] is thrown.
     *
     * If any of the [arguments] are invalid, then [ValidationError] is thrown.
     */
    override fun validate(environment: CallEnvironment) {
        environment.getFunction(functionName, arguments.size)
        arguments.forEach { it.validate(environment) }
    }

    /**
     * Returns a string representation of the function call in form of `"functionName(arg1,arg2)"`.
     */
    override fun toString(): String {
        return "$functionName(${arguments.joinToString(",")})"
    }
}
