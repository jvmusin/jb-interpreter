package jvmusin.interpreter.element

interface Element {
    operator fun invoke(environment: CallEnvironment): Int
    fun validate(environment: CallEnvironment)
}