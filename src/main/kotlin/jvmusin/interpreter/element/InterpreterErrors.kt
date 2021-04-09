package jvmusin.interpreter.element

abstract class WrappableError(
    message: String,
    cause: Throwable? = null,
    protected val wrapped: Boolean = false
) : Error(message, cause) {
    abstract val prefix: String
    protected fun wrapMessage(lineNumber: Int) = "$prefix $message:$lineNumber"
    protected inline fun <reified T : WrappableError> wrap(
        lineNumber: Int,
        constructor: (message: String, cause: Throwable?, wrapped: Boolean) -> T
    ): T {
        if (wrapped) return this as T
        return constructor(wrapMessage(lineNumber), this, true)
    }
}

open class ValidationError(
    message: String,
    cause: Throwable? = null,
    wrapped: Boolean = false
) : WrappableError(message, cause, wrapped) {
    override val prefix = "UNDEFINED VALIDATION ERROR"
    fun wrap(lineNumber: Int) = wrap(lineNumber, ::ValidationError)
}

class ParameterNotFoundError(message: String) : ValidationError(message) {
    override val prefix = "PARAMETER NOT FOUND"
}

class FunctionNotFoundError(message: String) : ValidationError(message) {
    override val prefix = "FUNCTION NOT FOUND"
}

class ArgumentNumberMismatchError(message: String) : ValidationError(message) {
    override val prefix = "ARGUMENT NUMBER MISMATCH"
}

class ArgumentNamesNotDistinct(message: String) : ValidationError(message) {
    override val prefix = "ARGUMENT NAMES NOT DISTINCT"
}

class FunctionNamesNotDistinct(message: String) : ValidationError(message) {
    override val prefix = "FUNCTION NAMES NOT DISTINCT"
}

class InterpreterRuntimeError(
    message: String,
    cause: Throwable? = null,
    wrapped: Boolean = false
) : WrappableError(message, cause, wrapped) {
    override val prefix = "RUNTIME ERROR"
    fun wrap(lineNumber: Int) = wrap(lineNumber, ::InterpreterRuntimeError)
}
