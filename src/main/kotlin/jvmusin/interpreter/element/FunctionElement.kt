package jvmusin.interpreter.element

/**
 * Function element.
 *
 * Represents a function with the given [name], [parameterNames] and [body] defined in some [lineNumber].
 */
data class FunctionElement(
    val lineNumber: Int,
    val name: String,
    val parameterNames: List<String>,
    val body: Element
) : Element {

    /**
     * Wraps invocation of the function with [invokeSafelyWithLineNumber]
     * to remember line number in case of some exceptions occur during function execution.
     *
     * Expects that [environment] already has all the function arguments with their respective values.
     */
    override fun invoke(environment: CallEnvironment) = invokeSafelyWithLineNumber(lineNumber) {
        invokeUnsafely(environment)
    }

    /**
     * Invokes the function's [body] with the given [environment] and returns result of the invocation.
     */
    override fun invokeUnsafely(environment: CallEnvironment) = body(environment)

    /**
     * Validates `this` function.
     *
     * Throws [ArgumentNamesNotDistinctError] in case of non-distinct function parameter names.
     *
     * Throws [ValidationError] if [body] is invalid.
     *
     * Expects that [environment] already has all the function arguments.
     * Argument values do not matter during the validation process.
     */
    override fun validate(environment: CallEnvironment) {
        if (parameterNames.distinct().size != parameterNames.size)
            throw ArgumentNamesNotDistinctError(name).wrap(lineNumber)
        body.validateWithLineNumber(environment, lineNumber)
    }

    /**
     * Returns string representation of this function.
     *
     * It looks like `"functionName(arg1,arg2)={body}"`.
     */
    override fun toString(): String {
        return "$name(${parameterNames.joinToString(",")})={$body}"
    }
}
