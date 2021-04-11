package jvmusin.interpreter.element

/**
 * Validates `this` [Element] using the concrete [lineNumber] and [environment].
 *
 * If validation fails, wraps the [ValidationError] using the given [lineNumber].
 */
fun Element.validateWithLineNumber(environment: CallEnvironment, lineNumber: Int) {
    try {
        validate(environment)
    } catch (e: ValidationError) {
        throw e.wrap(lineNumber)
    }
}

/**
 * Invokes [block] on `this` element safely.
 *
 * If some exception but [InterpreterRuntimeError] is thrown,
 * then wraps it into [InterpreterRuntimeError] with a message equal to `this.toString()`.
 *
 * Allows wrapping such that if some other function throws [InterpreterRuntimeError],
 * then it's being rethrown without any modifications.
 */
inline fun <T : Element> T.invokeSafely(block: T.() -> Int): Int {
    try {
        return block()
    } catch (e: InterpreterRuntimeError) {
        throw e
    } catch (e: Throwable) {
        throw InterpreterRuntimeError(toString(), e)
    }
}

/**
 * Invokes [block] on `this` element safely, preserving the [lineNumber].
 *
 * If some exception but [InterpreterRuntimeError] is thrown,
 * then wraps it into [InterpreterRuntimeError] with a message equal to `this.toString()`
 * and then immediately wraps it with [InterpreterRuntimeError.wrap] and [lineNumber].
 *
 * Allows wrapping such that if some other function throws [InterpreterRuntimeError],
 * then it's being rethrown without any modifications.
 * An exception here is only if the thrown [InterpreterRuntimeError] is not wrapped yet,
 * then it's being wrapped with [lineNumber].
 */
inline fun <T : Element> T.invokeSafelyWithLineNumber(lineNumber: Int, block: T.() -> Int): Int {
    try {
        return block()
    } catch (e: InterpreterRuntimeError) {
        throw e.wrap(lineNumber)
    } catch (e: Throwable) {
        throw InterpreterRuntimeError(toString(), e).wrap(lineNumber)
    }
}
