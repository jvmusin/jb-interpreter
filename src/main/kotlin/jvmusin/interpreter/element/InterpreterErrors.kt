package jvmusin.interpreter.element

/**
 * Wrappable error.
 *
 * An exception that allows to wrap cause of the exception by adding [prefix] and line number to it.
 *
 * @property wrapped `true` if the exception is already wrapped, or `false` otherwise.
 */
abstract class WrappableError(
    message: String,
    cause: Throwable? = null,
    protected val wrapped: Boolean = false
) : Error(message, cause) {

    /**
     * Prefix that will be added to the exception message when wrapping.
     */
    abstract val prefix: String

    /**
     * Wraps exception message by adding [prefix] and [lineNumber] to it.
     *
     * The result has form of `"prefix cause:lineNumber"`.
     */
    protected fun wrapMessage(lineNumber: Int) = "$prefix $message:$lineNumber"

    /**
     * Wraps current exception with a message provided by [wrap] method.
     *
     * Returns the exception itself if it's already wrapped
     * or the newly-created exception using [constructor] to build it.
     *
     * @param T Type of the exception to create.
     * @param lineNumber Line number to add to exception message.
     * @param constructor Constructor to use to create an exception.
     */
    protected inline fun <reified T : WrappableError> wrap(
        lineNumber: Int,
        constructor: (message: String, cause: Throwable?, wrapped: Boolean) -> T
    ): T {
        if (wrapped) return this as T
        return constructor(wrapMessage(lineNumber), this, true)
    }
}

/**
 * Validation error.
 *
 * Thrown if the program has logical (not syntactic) errors.
 *
 * Might be used if some function tries to use undefined variables or functions.
 *
 * This is a parent exception for all other validation errors.
 */
open class ValidationError(
    message: String,
    cause: Throwable? = null,
    wrapped: Boolean = false
) : WrappableError(message, cause, wrapped) {
    override val prefix = "UNDEFINED VALIDATION ERROR"
    fun wrap(lineNumber: Int) = wrap(lineNumber, ::ValidationError)
}

/**
 * Parameter not found error.
 *
 * Thrown if program tries to use undefined variable.
 */
class ParameterNotFoundError(variableName: String) : ValidationError(variableName) {
    override val prefix = "PARAMETER NOT FOUND"
}

/**
 * Function not found error.
 *
 * Thrown if program tries to invoke undefined function.
 */
class FunctionNotFoundError(functionName: String) : ValidationError(functionName) {
    override val prefix = "FUNCTION NOT FOUND"
}

/**
 * Argument number mismatch error.
 *
 * Thrown if program tries to invoke some defined function, but with different number of arguments.
 */
class ArgumentNumberMismatchError(functionName: String) : ValidationError(functionName) {
    override val prefix = "ARGUMENT NUMBER MISMATCH"
}

/**
 * Argument names not distinct.
 *
 * Thrown if some function's argument names are not pairwise-distinct.
 */
class ArgumentNamesNotDistinctError(duplicatedArgumentName: String) : ValidationError(duplicatedArgumentName) {
    override val prefix = "ARGUMENT NAMES NOT DISTINCT"
}

/**
 * Function names not distinct.
 *
 * Thrown if program defines several functions with the same name.
 */
class FunctionNamesNotDistinctError(functionName: String) : ValidationError(functionName) {
    override val prefix = "FUNCTION NAMES NOT DISTINCT"
}

/**
 * Interpreter runtime error.
 *
 * Thrown if some exception occurs during the program execution.
 */
class InterpreterRuntimeError(
    message: String,
    cause: Throwable? = null,
    wrapped: Boolean = false
) : WrappableError(message, cause, wrapped) {
    override val prefix = "RUNTIME ERROR"
    fun wrap(lineNumber: Int) = wrap(lineNumber, ::InterpreterRuntimeError)
}
