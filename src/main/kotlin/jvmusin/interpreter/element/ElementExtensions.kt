package jvmusin.interpreter.element

fun Element.validateWithLineNumber(environment: CallEnvironment, lineNumber: Int) {
    try {
        validate(environment)
    } catch (e: ValidationError) {
        throw e.wrap(lineNumber)
    }
}

inline fun <T : Element> T.invokeSafely(block: T.() -> Int): Int {
    try {
        return block()
    } catch (e: InterpreterRuntimeError) {
        throw e
    } catch (e: Throwable) {
        throw InterpreterRuntimeError(toString())
    }
}

inline fun <T : Element> T.invokeSafelyWithLineNumber(lineNumber: Int, block: T.() -> Int): Int {
    try {
        return block()
    } catch (e: InterpreterRuntimeError) {
        throw e.wrap(lineNumber)
    } catch (e: Throwable) {
        throw InterpreterRuntimeError(toString())
    }
}
