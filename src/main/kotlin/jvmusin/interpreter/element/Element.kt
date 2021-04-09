package jvmusin.interpreter.element

interface Element {
    operator fun invoke(environment: CallEnvironment) = invokeSafely { invokeUnsafely(environment) }
    fun invokeUnsafely(environment: CallEnvironment): Int
    fun validate(environment: CallEnvironment)
}
