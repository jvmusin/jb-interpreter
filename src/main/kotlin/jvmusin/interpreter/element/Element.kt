package jvmusin.interpreter.element

/**
 * Element of a program.
 */
interface Element {
    /**
     * Invokes this element with the given [environment] and returns result of this element invocation.
     *
     * Internally calls [invokeUnsafely] inside of [invokeSafely] to handle possible runtime errors.
     */
    operator fun invoke(environment: CallEnvironment) = invokeSafely { invokeUnsafely(environment) }

    /**
     * Invokes this element unsafely with the given [environment] and returns result of this element invocation.
     *
     * May produce any exceptions. They will be caught somewhere else.
     */
    fun invokeUnsafely(environment: CallEnvironment): Int

    /**
     * Validates the element using the given [environment].
     *
     * Does not call other functions, but calls all the possible ways in the invocation graph.
     *
     * If program structure is incorrect in some way, throws [ValidationError].
     */
    fun validate(environment: CallEnvironment)
}
