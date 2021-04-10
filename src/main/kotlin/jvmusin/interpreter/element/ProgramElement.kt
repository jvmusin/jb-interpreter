package jvmusin.interpreter.element

/**
 * Program element.
 *
 * Represents the whole program, including all defined [functions] and program's [body].
 *
 * Before starting the program, run parameter-less [validate] function to ensure
 * that the program is valid.
 *
 * To run the program, call parameter-less [invoke] function.
 * Result of this function is the result of the program invocation.
 */
data class ProgramElement(private val functions: List<FunctionElement>, private val body: Element) : Element {

    /**
     * Body line number.
     *
     * Assumes that all functions has line numbers from `1` to `functions.size`,
     * so the body line number is equal to `functions.size + 1`.
     */
    private val bodyLineNumber = functions.size + 1

    /**
     * An environment that contains all defined functions and no variables.
     */
    private val startEnvironment = CallEnvironment(Functions(functions.associateBy { it.name }), Variables(emptyMap()))

    /**
     * Starts the program.
     */
    operator fun invoke() = invoke(startEnvironment)

    /**
     * Starts the program safely with [invokeSafelyWithLineNumber] to maintain the number of failure in case of runtime errors.
     *
     * Completely ignores [environment] argument.
     */
    override fun invoke(environment: CallEnvironment) = invokeSafelyWithLineNumber(bodyLineNumber) {
        invokeUnsafely(startEnvironment)
    }

    /**
     * Starts the program unsafely. May throw an exception that should be caught outside of this function.
     *
     * Completely ignores [environment] argument.
     */
    override fun invokeUnsafely(environment: CallEnvironment) = body(startEnvironment)

    /**
     * Validates the program's [functions] and [body] and throws [ValidationError] in case of validation failure.
     *
     * Completely ignores [environment] argument.
     */
    override fun validate(environment: CallEnvironment) = validate()

    /**
     * Validates the program's [functions] and [body] and throws [ValidationError] in case of validation failure.
     */
    fun validate() {
        val foundFunctionNames = mutableSetOf<String>()
        for (function in functions) {
            if (!foundFunctionNames.add(function.name)) {
                throw FunctionNamesNotDistinctError(function.name).wrap(function.lineNumber)
            }
            val variables = Variables(function.parameterNames.associateWith { 0 })
            function.validate(startEnvironment.copy(variables = variables))
        }
        body.validateWithLineNumber(startEnvironment, bodyLineNumber)
    }

    /**
     * Returns string representation of the program.
     *
     * The program contains defined [functions] first and the the program's [body]:
     *
     * ```
     * fun1(a,b,c)={body1}
     * fun2(a,b)={body2}
     * fun3(x,y)={body3}
     * programBody
     * ```
     */
    override fun toString(): String {
        return functions.joinToString("") { it.toString() + "\n" } + body.toString()
    }
}
