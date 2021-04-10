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
        val argumentValues = arguments.map { it.invoke(environment) }
        val function = environment.getFunction(functionName)
        val parameterNames = function.parameterNames
        return function(environment.copy(variables = Variables(parameterNames.zip(argumentValues).toMap())))
    }

    /**
     * Validates all the [arguments] using [environment].
     *
     * If some of arguments are invalid, then [ValidationError] is thrown.
     *
     * If argument count is not equal to the callable function's argument count,
     * then [ArgumentNumberMismatchError] is thrown.
     */
    override fun validate(environment: CallEnvironment) {
        arguments.forEach { it.validate(environment) }
        if (environment.getFunction(functionName).parameterNames.size != arguments.size)
            throw ArgumentNumberMismatchError(functionName)
    }

    /**
     * Returns a string representation of the function call in form of `"functionName(arg1,arg2)"`.
     */
    override fun toString(): String {
        return "$functionName(${arguments.joinToString(",")})"
    }
}
